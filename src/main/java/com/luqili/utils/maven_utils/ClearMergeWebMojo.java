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
 * 根据日志清理拷贝到目标的文件
 * 
 * @author luqili 2017年6月6日 下午10:36:31
 */
@Mojo(name = "clear-merge", defaultPhase = LifecyclePhase.PRE_CLEAN)
public class ClearMergeWebMojo extends AbstractMojo {

  @Parameter(property = "project", required = true)
  private MavenProject project;

  private int delCount=0;
  private int filterCount=0;
  /**
   * 文件排除的后缀
   */
  @Parameter(property = "suffixExcludes", required = false)
  private List<String> suffixExcludes;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    File logFile = HandleFileTool.getMergeFileLogsFile(project);
    if (logFile.exists() && logFile.isFile()) {
      try {
        List<String> fileLogs = FileUtils.readLines(logFile, StandardCharsets.UTF_8);
        List<File> dirs = new ArrayList<>();
        // 删除拷贝来的文件
        for (String fileLog : fileLogs) {
          File file = new File(fileLog);
          String fileName = file.getName();
          if (!file.exists()) {
            continue;
          }
          if (file.isDirectory()) {
            dirs.add(file);
          } else {
            // 过滤掉的后缀不进行删除
            boolean isDel = true;
            if (suffixExcludes != null && !suffixExcludes.isEmpty()) {
              for (String su : suffixExcludes) {
                if (fileName.toLowerCase().endsWith(su.toLowerCase().trim())) {
                  isDel = false;
                }
              }
            }
            if (isDel) {
              file.delete();
              getLog().info("删除:" + file.getAbsolutePath());
              delCount++;
            } else {
              getLog().info("跳过:" + file.getAbsolutePath());
              filterCount++;
            }
          }
        }
        // 统一处理文件夹，删除拷贝来的文件夹(包含文件的文件夹保留)

        for (File file : dirs) {
          if (!file.exists()) {
            continue;
          }
          boolean canDel = HandleFileTool.isEmptyDirctory(file);
          if (canDel) {
            getLog().info("删除空文件夹:" + file.getAbsolutePath());
            FileUtils.deleteQuietly(file);
          } else {
            getLog().info("非空文件夹:" + file.getAbsolutePath());
          }
        }
      } catch (IOException e) {
        getLog().error("清理文件异常：" + e.getMessage());
      }
      logFile.delete();
      getLog().info("删除日志记录:" + logFile.getAbsolutePath());
      getLog().info("共计删除:" + delCount+"个,跳过:"+filterCount+"个");
      HandleFileTool.clearRootPath(project);
    }
  }

}
