//============================================================================
// Name        : wolfgang.cpp
// Author      : Adrian Lange
// Version     :
// Copyright   : 2015 IIG Uni Freiburg
// Description : Starter application for Wolfgang.
//============================================================================

#include <iostream>
#include <stdlib.h>
#include <string>
#include <sys/stat.h>
using namespace std;

const string jar_path = "/usr/share/wolfgang/wolfgang.jar";
const string class_name = "de.uni.freiburg.iig.telematik.wolfgang.WolfgangStartup";

inline bool file_exists (const string& name) {
  struct stat buffer;
  return (stat (name.c_str(), &buffer) == 0);
}

int main(int argc, char* argv[]) {
	bool jar_exists = file_exists(jar_path);
	if (jar_exists) {
		string arguments = "";
		if (argc >= 2) {
			arguments = argv[1];
		}
		system( ("java -cp " + jar_path + " " + class_name + " " + arguments).c_str() );
	} else {
		cerr << "Couldn't find file '" + jar_path + "'." << endl;
	}
	return 0;
}
