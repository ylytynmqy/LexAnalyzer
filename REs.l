digit   [0-9]
letter  [A-Za-z]

%%

"(\\.|.)*" {
    System.out.println("<[String], " + match+">");
}
;|\{|\}|\(|\)|\[|\]|"|, {
    System.out.println("<[Delimiter], " + match+">");
}
\+|-|\*|/|%|~|!|\+\+|--|<<|>>|>>>|<|>|<=|>=|==|!=|&|^|\||&&|\|\||?|:|=|\+=|-=|\*=|/=|%=|&=|^=|\|=|<<=|>>=|>>>=|\. {
    System.out.println("<[Operator], " + match+">");
}
/\*(\*|.)*\*/ {
    System.out.println("<[Multiline Comment], " + match+">");
}
//.*(\r|\n) {
    System.out.println("<[Singleline Comment], " + match+">");
}
{digit}+(\.{digit}*)?(L|d|f)? {
    System.out.println("<[Number], " + match+">");
}
0x[0-9A-Fa-f]+ {
    System.out.println("<[Hex], " + match+">");
}
({letter}|_)({letter}|{digit}|_)* {
    System.out.println("<[Identifier], " + match+">");
}
abstract{
    System.out.println("<[ABSTRACT], " + match+">");
}
assert{
    System.out.println("<[ASSERT], " + match+">");
}
boolean{
    System.out.println("<[BOOLEAN], " + match+">");
}
break{
    System.out.println("<[BREAK], " + match+">");
}
byte{
    System.out.println("<[BYTE], " + match+">");
}
case{
    System.out.println("<[CASE], " + match+">");
}
catch{
    System.out.println("<[CATCH], " + match+">");
}
char{
    System.out.println("<[CHAR], " + match+">");
}
class{
    System.out.println("<[CLASS], " + match+">");
}
const{
    System.out.println("<[CONST], " + match+">");
}
continue{
    System.out.println("<[CONTINUE], " + match+">");
}
default{
    System.out.println("<[DEFAULT], " + match+">");
}
do{
    System.out.println("<[DO], " + match+">");
}
double{
    System.out.println("<[DOUBLE], " + match+">");
}
else{
    System.out.println("<[ELSE], " + match+">");
}
enum{
    System.out.println("<[ENUM], " + match+">");
}
extends{
    System.out.println("<[EXTENDS], " + match+">");
}
final{
    System.out.println("<[FINAL], " + match+">");
}
finally{
    System.out.println("<[FINALLY], " + match+">");
}
float{
    System.out.println("<[FLOAT], " + match+">");
}
for{
    System.out.println("<[FOR], " + match+">");
}
goto{
    System.out.println("<[GOTO], " + match+">");
}
if{
    System.out.println("<[IF], " + match+">");
}
implements{
    System.out.println("<[IMPLEMENTS], " + match+">");
}
import{
    System.out.println("<[IMPORT], " + match+">");
}
instanceof{
    System.out.println("<[INSTANCEOF], " + match+">");
}
int{
    System.out.println("<[INT], " + match+">");
}
interface{
    System.out.println("<[INTERFACE], " + match+">");
}
long{
    System.out.println("<[LONG], " + match+">");
}
native{
    System.out.println("<[NATIVE], " + match+">");
}
new{
    System.out.println("<[NEW], " + match+">");
}
package{
    System.out.println("<[PACKAGE], " + match+">");
}
private{
    System.out.println("<[PRIVATE], " + match+">");
}
protected{
    System.out.println("<[PROTECTED], " + match+">");
}
public{
    System.out.println("<[PUBLIC], " + match+">");
}
return{
    System.out.println("<[RETURN], " + match+">");
}
short{
    System.out.println("<[SHORT], " + match+">");
}
static{
    System.out.println("<[STATIC], " + match+">");
}
strictfp{
    System.out.println("<[STRICTFP], " + match+">");
}
super{
    System.out.println("<[SUPER], " + match+">");
}
switch{
    System.out.println("<[SWITCH], " + match+">");
}
synchronized{
    System.out.println("<[SYNCHRONIZED], " + match+">");
}
this{
    System.out.println("<[THIS], " + match+">");
}
throw{
    System.out.println("<[THROW], " + match+">");
}
throws{
    System.out.println("<[THROWS], " + match+">");
}
transient{
    System.out.println("<[TRANSIENT], " + match+">");
}
try{
    System.out.println("<[TRY], " + match+">");
}
void{
    System.out.println("<[VOID], " + match+">");
}
volatile{
    System.out.println("<[VOLATILE], " + match+">");
}
while{
    System.out.println("<[WHILE], " + match+">");
}