package com.luqili.utils.maven_utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
/**
 * 合并两个Web项目工程
 * <p>outProjectName 不为空时，从当前项目发布复制到指定项目</p>
 * <p>srcProjectName 不为空时，从指定项目拉取到当前项目</p>
 * <p>两者不能同时存在</p>
 * @author luqili
 *
 */
@Mojo(name="merge",defaultPhase=LifecyclePhase.GENERATE_SOURCES)
public class MergeWebMojo extends AbstractMojo {
	@Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
	private File outputDirectory;
	
	@Parameter(property="project",required=true)
	private MavenProject project;
	
	/**
	 * 指定来源项目（与当前项目同级目录）
	 */
	@Parameter(property="srcProjectName",required=false)
	private String srcProjectName;
	/**
	 * 指定来源项目路径
	 */
	@Parameter(property="srcProjectFile",required=false)
	private File srcProjectFile;
	/**
	 * 指定输出项目（与当前项目同级目录）
	 */
	@Parameter(property="outProjectName",required=false)
	private String outProjectName;
	/**
	 * 指定输出项目路径
	 */
	@Parameter(property="outProjectFile",required=false)
	private File outProjectFile;
	/**
	 * 文件包含的后缀
	 */
	@Parameter(property="suffixIncludes",required=false)
	private List<String> suffixIncludes;
	/**
	 * 文件排除的后缀
	 */
	@Parameter(property="suffixExcludes",required=false)
	private List<String> suffixExcludes;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		File baseDir = project.getBasedir();
		if(srcProjectFile==null){
			if(StringUtils.isNotBlank(srcProjectName)){
				File[] fs=baseDir.getParentFile().listFiles();
				for(File f:fs){
					if(StringUtils.equalsIgnoreCase(srcProjectName.trim(), f.getName())){
						srcProjectFile=f;
					}
				}
			}
		}
		if(outProjectFile==null){
			if(StringUtils.isNotBlank(outProjectName)){
				File[] fs=baseDir.getParentFile().listFiles();
				for(File f:fs){
					if(StringUtils.equalsIgnoreCase(outProjectName.trim(), f.getName())){
						outProjectFile=f;
					}
				}
			}
		}
		if(srcProjectFile==null && outProjectFile==null){
			String msg="请指定(来源项目名或项目路径、输出项目名或项目路径)。";
			getLog().error(msg);
			throw new MojoFailureException(msg);
		}
		if(srcProjectFile!=null && outProjectFile!=null){
			String msg="(来源项目名或项目路径 与 输出项目名或项目路径)不能同时存在。";
			getLog().error(msg);
			throw new MojoFailureException(msg);
		}
		if(srcProjectFile==null){
			srcProjectFile=baseDir;
		}else{
			outProjectFile=baseDir;
		}
		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File pathname) {
				boolean result = true;
				String fileName=pathname.getName();
				if(suffixIncludes!=null&&!suffixIncludes.isEmpty()){
					result=false;
					for(String su:suffixIncludes){
						if(fileName.toLowerCase().endsWith(su.toLowerCase().trim())){
							result=true;
						}
					}
					if(pathname.isDirectory() && ArrayUtils.isNotEmpty(pathname.listFiles())){
						//允许非空文件夹通过
						result=true;
					}
				}
				//不允许通过的后缀
				if(suffixExcludes!=null&&!suffixExcludes.isEmpty()){
					for(String su:suffixExcludes){
						if(fileName.toLowerCase().endsWith(su.toLowerCase().trim())){
							result=false;
						}
					}
				}
				if(result){
					getLog().info("复制文件或目录:"+pathname.getAbsolutePath());
				}
				return result;
			}
		};
		
		try {
			FileUtils.copyDirectory(srcProjectFile, outProjectFile,fileFilter);
		} catch (IOException e) {
			String msg="复制文件错误:"+e.getMessage();
			getLog().error(msg);
			throw new MojoFailureException(msg);
		}
		getLog().info("项目来源目录:"+srcProjectFile.getAbsolutePath());
		getLog().info("项目输出目录:"+outProjectFile.getAbsolutePath());
	}

}