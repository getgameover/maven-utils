package com.luqili.utils.maven_utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * 清理合并的文件
 * @author luqili 2017年6月6日 下午10:36:31
 *
 */
@Mojo(name="clear-merge",defaultPhase=LifecyclePhase.PRE_CLEAN)
public class ClearMergeWebMojo extends AbstractMojo {
  
  @Parameter(property="project",required=true)
  private MavenProject project;
  
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    File logFile = HandleFileTool.getMergeFileLogsFile(project);
    if(logFile.exists() && logFile.isFile()){
      try {
        List<String> fileLogs = FileUtils.readLines(logFile,StandardCharsets.UTF_8);
        List<File> dirs = new ArrayList<>();
        //删除文件
        for(String fileLog:fileLogs){
          File file = new File(fileLog);
          if(file.isDirectory()){
            dirs.add(file);
          }else{
            getLog().info("删除历史文件:"+file.getAbsolutePath());
            file.delete();
          }
        }
        //统一处理文件夹，删除空文件夹
        for(File file:dirs){
          if(file.list()==null||file.list().length<1){
            getLog().info("删除空文件夹:"+file.getAbsolutePath());
            file.delete();
          }
        }
      } catch (IOException e) {
        getLog().error("清理文件失败："+e.getMessage());
      }
     
    }
  }

}
