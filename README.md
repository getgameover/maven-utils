## maven-utils

#### Maven编译项目自定义工具插件工具插件

###适用场景
1. 在一个大maven项目(WEB项目)中,会拆分成多个模块，很有可能会把js、css、jsp等文件放在不同的模块中（根据业务模块划分）。在测试、最终打包时非常的不方便。使用merge可以经相关文件合并到一个项目中，方便测试、打包、js,css压缩等操作。使用clear-merge也可以随时撤销合并。
1. 在WEB项目中，一般基础部分是相同的，如果有很多个WEB项目时使用同一个基础部分。当基础部分更新时，会非常的麻烦。使用merge则直接可以经基础工程覆盖到当前项目中，方便开发测试等。

### 插件说明:
1. `<goal>merge</goal>`插件merge 合并两个项目源文件到指定的项目中去（两个模块中不能存在目录和文件名完全相同的文件，否则其中一个会被覆盖）。
1. `<goal>clear-merge</goal>`插件clear-merge 清理合并来的源文件信息。
1. 本项目未发布到公有Maven库中，使用者需要通过Git下载，通过Maven编译后进行使用 

##### 配置XML说明：
```xml
<plugins>
	<plugin>
		<artifactId>maven-utils</artifactId>
		<groupId>com.luqili.utils</groupId>
		<version>0.0.2</version>
		<executions>
			<execution>
				<id>copy-file</id>
				<!--<phase>generate-sources</phase> 默认调用阶段 -->
				<goals>
					<goal>merge</goal><!-- 调用合并插件 -->
				</goals>
			</execution>
			<execution>
				<id>clear-copy-file</id>
				<!--<phase>pre-clean</phase> 默认调用阶段 -->
				<goals>
					<goal>clear-merge</goal><!-- 调用清理插件 -->
				</goals>
			</execution>
		</executions>
		<configuration>
			<!-- 将项目weixin-java-common（必须与本项目在同一个相同的文件夹下）源文件复制到当前项目 推荐方式 -->
			<srcProjectName>weixin-java-common</srcProjectName>			
			<!-- 指定来源项目的文件夹，优先级高于srcProjectName -->
			<!-- <srcProjectFile>/home/xxx/git/weixin-java-common</srcProjectFile> -->			
			<!-- 存在src信息时为将 src 项目下的文件复制到当前项目下 -->
			<!-- 将本项目源文件复制到项目weixin-java-common（必须与本项目在同一个相同的文件夹下） 推荐方式 -->
			<!-- <outProjectName>weixin-java-common</</outProjectName> -->
			<!-- 指定输出项目的文件夹，优先级高于outProjectName -->
			<!-- <outProjectFile>/home/xxx/git/weixin-java-common</outProjectFile> -->			
			<!-- 只复制文件(名称以‘suffixInclude’结尾的) -->
			<!-- 
			<suffixIncludes>
				<suffixInclude>.java</suffixInclude>
				<suffixInclude>.js</suffixInclude>
				<suffixInclude>.css</suffixInclude>
				<suffixInclude>.img</suffixInclude>
				<suffixInclude>.txt</suffixInclude>
			</suffixIncludes>
			 -->
			<!-- 过滤文件、文件夹(名称以‘suffixExclude’结尾的),优先级高于suffixIncludes -->
			<suffixExcludes>
				<suffixExclude>target</suffixExclude><!-- 不复制 target文件夹及该文件夹下所有的文件 -->
				<suffixExclude>.class</suffixExclude><!-- 不复制 后缀为.class的文件 -->
				<suffixExclude>.project</suffixExclude>
				<suffixExclude>.settings</suffixExclude>
				<suffixExclude>.classpath</suffixExclude>
				<suffixExclude>pom.xml</suffixExclude><!-- 不复制 pom.xml的文件 或 xxxpom.xml -->
				<suffixExclude>.gradle</suffixExclude>
				<suffixExclude>test</suffixExclude>
			</suffixExcludes>
		</configuration>
	</plugin>
</plugins>
```
##### 使用样例:
```xml
<build>
	<plugins>
		<plugin>
			<artifactId>maven-utils</artifactId>
			<groupId>com.luqili.utils</groupId>
			<version>0.0.2</version>
			<executions>
				<execution>
					<id>copy-file</id>
					<goals>
						<goal>merge</goal><!-- 调用合并插件 -->
						<goal>clear-merge</goal><!-- 调用清理插件 -->
					</goals>
				</execution>
			</executions>
			<configuration>
				<srcProjectName>weixin-java-common</srcProjectName><!-- 将项目weixin-java-common源文件复制到当前项目 -->
				<suffixExcludes><!-- 过滤文件、文件夹(名称以‘suffixExclude’结尾的)-->
					<suffixExclude>target</suffixExclude><!-- 不复制 target文件夹及该文件夹下所有的文件 -->
					<suffixExclude>.class</suffixExclude><!-- 不复制 后缀为.class的文件 -->
					<suffixExclude>.project</suffixExclude>
					<suffixExclude>.settings</suffixExclude>
					<suffixExclude>.classpath</suffixExclude>
					<suffixExclude>pom.xml</suffixExclude><!-- 不复制 pom.xml的文件 或 xxxpom.xml -->
					<suffixExclude>.gradle</suffixExclude>
					<suffixExclude>test</suffixExclude>
				</suffixExcludes>
			</configuration>
		</plugin>
	</plugins>
</build>
```
***
