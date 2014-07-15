/*
 * implementation of stdio.h of the c standard-lib
 */

//------------------- Forward declarations

void putc(char ch);
void prints(string s);

string itoa(int x);

//------------------- declarations

// print character
void putc(char ch) {
    print(ch);
}

// print string
void prints(string s) {
    int i;
    i = 0;    
    while(i < length(s)) {
        putc(s[i];
        i = i+1;    
    }
}

string itoa(int x) {
    string s;
    s = "";

    while(x != 0) {
        s = (string)((x%10)+48) + s;
        x = x % 10;
    }

    return s;
}