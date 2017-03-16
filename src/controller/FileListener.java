package controller;

import java.io.File;

public interface FileListener {
	abstract void fileChanged(File newFile);
}
