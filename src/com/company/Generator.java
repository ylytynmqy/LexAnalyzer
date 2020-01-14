package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Generator {
    Generator() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("REs.l"));
            Map<String, List<RegNode>> reNameMap = new HashMap<>();
            List<List<RegNode>> regexList = new ArrayList<>();
            List<String> codeList = new ArrayList<>();

            int state = 0;
            String tag="";
            String regex = "";
            StringBuilder code = new StringBuilder();

            int temp = 0;
            //convert manual REs.l to Regex
            while (true) {
                String line=reader.readLine();
                if(line==null){
                    break;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                switch (state) {
                    case 0:
                        if (line.equals("%%")) {
                            state = 1;
                            continue;
                        }
                        String[] list1 = line.split(" ", 2);
                        tag = list1[0];
                        regex = list1[1].trim();
                        List<RegNode> parsedRE = Regex.parse(regex, reNameMap);
                        reNameMap.put(tag, parsedRE);
                        break;
                    case 1:
                        regex = line.substring(0, line.length() - 1).trim();
                        temp = 0;
                        state = 2;
                        break;
                    case 2:
                        //process the } line
                        if (line.equals("}")) {
                            if (temp == 0) {
                                regexList.add(Regex.parse(regex, reNameMap));
                                codeList.add(code.toString());
                                code = new StringBuilder();
                                state = 1;
                                continue;
                            } else {
                                temp=temp-1;
                            }
                        }
                        code.append(line).append("\r\n");
                        for (char c : line.toCharArray()) {
                            if (c == '{') {
                                temp=temp+1;
                            } else if (c == '}') {
                                temp=temp-1;
                            }
                        }
                }
            }

            //Convert Regex to NFA
            NumberProcessing numberProcessing = new NumberProcessing();
            List<NFA> nfaList = new ArrayList<>();
            for (int i = 0; i < regexList.size(); i++) {
                nfaList.add(NFA.process(i, regexList.get(i), numberProcessing));
            }
            //Convert NFA to DFA*
            List<DFA> dfaList = DFA.process(NFA.merge(nfaList, numberProcessing));

            //DFA* to codes
            InputStream inputStream = Main.class.getResourceAsStream("template");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String template = new String(buffer);

            StringBuilder sb = new StringBuilder();
            Map<String, Integer> paramsMap = new HashMap<>();
            Map<Integer, Integer> addDFAMap = new HashMap<>();
            for (int i = 0; i < dfaList.size(); i++) {
                StringBuilder params = new StringBuilder();
                params.append(dfaList.get(i).tag);
                for (Map.Entry<Character, DFA> entry : dfaList.get(i).following.entrySet()) {
                    params.append(",").append(((int) entry.getKey())).append(",").append(entry.getValue().Number);
                }
                Integer mIndex = paramsMap.get(params.toString());
                if (mIndex == null) {
                    paramsMap.put(params.toString(), i);
                    mIndex = i;
                    sb.append("    private static void caseDFA").append(i).append("(){\r\n        geneDFA(");
                    sb.append(params);
                    sb.append(");\r\n    }\r\n");
                }
                addDFAMap.put(i, mIndex);
            }
            sb.append("    static{\r\n");
            for (int i = 0; i < dfaList.size(); i++) {
                sb.append("        caseDFA").append(addDFAMap.get(i)).append("();\r\n");
            }
            sb.append("}\r\n");
            template = template.replace("/*generate*/", sb.toString());

            sb = new StringBuilder();
            for (int i = 0; i < codeList.size(); i++) {
                sb.append("        case ").append(i).append(":\r\n").append("            ").append(codeList.get(i)).append("            break;\r\n");
            }
            template = template.replace("/*case*/", sb.toString());

            //Generate code
            FileOutputStream fileOutputStream = new FileOutputStream("./src/com/company/Lexer.java");
            fileOutputStream.write(template.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

