package com.luqili.utils.maven_utils;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.maven.project.MavenProject;

public class HandleFileTool {
	/**
	 * 文件夹
	 */
	private static final String DIR_BASE = "maven-utils";
	/**
	 * 默认目录
	 */
	private static final String FILE_MERGE_FILE_LOGS = "copy_file.log";

	/**
	 * 获得合并文件日志记录文件
	 * 
	 * @param project
	 * @return
	 */
	public static File getMergeFileLogsFile(MavenProject project) {
		String baseDir = getPlugRootPath(project);
		return new File(baseDir + "/" + FILE_MERGE_FILE_LOGS);
	}

	/**
	 * 获得根目录
	 * 
	 * @param project
	 * @return
	 */
	public static String getPlugRootPath(MavenProject project) {
		return project.getBasedir().getAbsolutePath() + "/target/" + DIR_BASE;
	}

	/**
	 * 清理根目录
	 * 
	 * @param project
	 */
	public static void clearRootPath(MavenProject project) {
		String rootPath = getPlugRootPath(project);
		File root = new File(rootPath);
		if (root.isDirectory()) {
			if (isEmptyDirctory(root)) {
				root.delete();
			}
		}
	}

	/**
	 * 判断当前文件夹是否为空文件夹(不包含文件的文件夹)
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isEmptyDirctory(File file) {
		IOFileFilter noFilter = new IOFileFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return true;
			}
			@Override
			public boolean accept(File file) {
				return true;
			}
		};
		Collection<File> fs = FileUtils.listFiles(file, noFilter, noFilter);
		boolean result = true;
		for (File f : fs) {
			if (f.exists() && f.isFile()) {
				result = false;
			}
		}
		return result;
	}
}
