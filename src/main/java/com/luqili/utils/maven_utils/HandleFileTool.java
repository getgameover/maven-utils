package com.luqili.utils.maven_utils;

import java.io.File;

import org.apache.maven.project.MavenProject;

public class HandleFileTool {
  /**
   * 文件夹
   */
  private static final String DIR_BASE="maven-utils";
  /**
   * 默认目录
   */
  private static final String MERGE_FILE_LOGS="copy_file.log"; 
  
  /**
   * 获得合并文件日志记录文件
   * @param project
   * @return
   */
  public static File getMergeFileLogsFile(MavenProject project){
    String baseDir=project.getBasedir().getAbsolutePath()+"/target/"+DIR_BASE;
    return new File(baseDir+"/"+MERGE_FILE_LOGS);
  }
}
