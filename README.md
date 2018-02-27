###**Allatori混淆技术介绍** 
Allatori是一个Java 混淆器，它属于第二代混淆器，因此它能够全方位的保护你的知识产权。 Allatori具有以下几种保护方式：命名混淆，流混淆，调试信息混淆，字符串混淆，以及水印技术。对于教育和非商业项目来说这个混淆器是免费的。支持war和jar文件格式，并且允许对需要混淆代码的应用程序添加有效日期。 有项目需要对代码进行保护，比较初级的方案就是对代码进行混淆，打包之后的文件进行反编译后，就可以看到效果。此外，使用Allatori打的包大小也会小一点。

###**工程介绍** 
![这里写图片描述](http://img.blog.csdn.net/20180227105813973?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcmlja2l5ZWF0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

一个很普通的maven工程，不同的是在根目录下加入Allatori的jar包。

#####**下面我们来看看pom.xml文件：**

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.lovnx</groupId>
	<artifactId>confusion</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			<!-- Allatori plugin start -->
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-resources-plugin</artifactId>
				<version>2.6</version>
				<executions>
					<execution>
						<id>copy-and-filter-allatori-config</id>
						<phase>package</phase>
						<goals>
							<goal>copy-resources</goal>
						</goals>
						<configuration>
							<outputDirectory>${basedir}/target</outputDirectory>
							<resources>
								<resource>
									<directory>${basedir}/allatori</directory>
									<includes>
										<include>allatori.xml</include>
									</includes>
									<filtering>true</filtering>
								</resource>
							</resources>
						</configuration>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>exec-maven-plugin</artifactId>
				<version>1.2.1</version>
				<executions>
					<execution>
						<id>run-allatori</id>
						<phase>package</phase>
						<goals>
							<goal>exec</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<executable>java</executable>
					<arguments>
						<argument>-Xms128m</argument>
						<argument>-Xmx512m</argument>
						<argument>-jar</argument>
						<argument>${basedir}/lib/allatori.jar</argument>
						<argument>${basedir}/target/allatori.xml</argument>
					</arguments>
				</configuration>
			</plugin>
			<!-- Allatori plugin end -->
		</plugins>
	</build>

	<dependencies>
		<!-- Test Begin -->
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<scope>test</scope>
		</dependency>
		<!-- Test End -->
		<!-- springboot启动 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
	</dependencies>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.8.RELEASE</version>
	</parent>
</project>
```
使用maven打包插件，Spring Boot构建的工程，Allatori的配置在上面也有说明，Allatori配置里面比较重要的是：
				

```
		<argument>${basedir}/lib/allatori.jar</argument>
		<argument>${basedir}/target/allatori.xml</argument>
```
指定Allatori的allatori.jar文件路径，如果你的工程是一个pom工程，可以在父工程中放lib目录，然后子工程只需要：
```
		<argument>../lib/allatori.jar</argument>
```

即可。

#####**allatori.xml这个文件也很重要，看看其中的内容：**

```
<config>
    <input>
        <jar in="confusion-0.0.1-SNAPSHOT.jar" out="confusion-0.0.1-SNAPSHOT-obfuscated.jar"/>
    </input>

    <keep-names>
        <class access="protected+">
            <field access="protected+"/>
            <method access="protected+"/>
        </class>
    </keep-names>

    <property name="log-file" value="log.xml"/>
</config>
```
即是对Allatori混淆器的具体配置，这里可以配置很多信息，很多种策略，也可以指定哪些类不被混淆，具体的各种方式可以在在文末附件里面的文档得到。
这里需要说明的是：
```
 <input>
        <jar in="confusion-0.0.1-SNAPSHOT.jar" out="confusion-0.0.1-SNAPSHOT-obfuscated.jar"/>
 </input>
```
`confusion-0.0.1-SNAPSHOT.jar`这个是打包后的未被混淆的包，而`confusion-0.0.1-SNAPSHOT-obfuscated.jar`是混淆后的包，这个是我们需要的。

###**打包步骤** 
**1、clean maven工程。**
**2、将resources下面的allatori.xml文件复制到target目录下面。**
**3、install maven工程，看到如下信息后表示成功：**

```
################################################
#                                              #
#        ## #   #    ## ### ### ##  ###        #
#       # # #   #   # #  #  # # # #  #         #
#       ### #   #   ###  #  # # ##   #         #
#       # # ### ### # #  #  ### # # ###        #
#                                              #
#                DEMO VERSION!                 #
#           NOT FOR COMMERCIAL USE!            #
#                                              #
#       Demo version adds System.out's         #
#       and gives 'ALLATORI_DEMO' name         #
#       to some fields and methods.            #
#                                              #
#                                              #
# Obfuscation by Allatori Obfuscator v6.4 DEMO #
#                                              #
#           http://www.allatori.com            #
#                                              #
################################################
```

**4、成功后的工程：**

![这里写图片描述](http://img.blog.csdn.net/20180227111358864?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcmlja2l5ZWF0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

箭头所指处即是我们需要的包，此包代码已被混淆。

###**效果查看** 
这里使用反编译工具对混淆后的包进行查看，我用的是jd-gui这个软件，小巧实用。

**TestApplication.java混淆前：**

```
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication {
	
	public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
	}
}
```

**TestApplication.java混淆后：**

```
import java.io.PrintStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication
{
  public static String ALLATORIxDEMO(String a)
  {
    int tmp4_3 = 4;
    int tmp7_6 = 1;
    int tmp21_18 = a.length();
    int tmp25_24 = 1;
    tmp25_24;
    int j;
    int ? = tmp25_24;
    int k = tmp21_18;
    int tmp35_31 = (j = new char[tmp21_18] - 1);
    tmp35_31;
    int i = 5 << 4 ^ (0x2 ^ 0x5);
    (tmp4_3 << tmp4_3 ^ tmp7_6 << tmp7_6);
    if (tmp35_31 >= 0)
    {
      int tmp45_44 = j;
      j--;
      ?[tmp45_44] = ((char)(a.charAt(tmp45_44) ^ i));
      int tmp66_63 = (j--);
      ?[tmp66_63] = ((char)(a.charAt(tmp66_63) ^ k));
    }
    return new String(?);
  }

  public static void main(String[] a)
  {
    System.out.println("\n################################################\n#                                              #\n#        ## #   #    ## ### ### ##  ###        #\n#       # # #   #   # #  #  # # # #  #         #\n#       ### #   #   ###  #  # # ##   #         #\n#       # # ### ### # #  #  ### # # ###        #\n#                                              #\n# Obfuscation by Allatori Obfuscator v6.4 DEMO #\n#                                              #\n#           http://www.allatori.com            #\n#                                              #\n################################################\n"); SpringApplication.run(TestApplication.class, a);
  }
}
```

**TestController.java混淆前：**

```
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/test")
	public String test(){
		return "88888888888888888";
	}
}
```

**TestController.java混淆后：**

```
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController
{
  @GetMapping({"/test"})
  public String test()
  {
    return ALLATORIxDEMO("*]*]*]*]*]*]*]*]*");
  }

  public static String ALLATORIxDEMO(String a)
  {
    int tmp27_24 = a.length();
    int tmp31_30 = 1;
    tmp31_30;
    int j;
    int ? = tmp31_30;
    int k = tmp27_24;
    int tmp41_37 = (j = new char[tmp27_24] - 1);
    tmp41_37;
    int i = (0x3 ^ 0x5) << 4 ^ 0x5;
    (2 << 3 ^ 0x2);
    if (tmp41_37 >= 0)
    {
      int tmp51_50 = j;
      j--;
      ?[tmp51_50] = ((char)(a.charAt(tmp51_50) ^ i));
      int tmp72_69 = (j--);
      ?[tmp72_69] = ((char)(a.charAt(tmp72_69) ^ k));
    }
    return new String(?);
  }
}
```

哈哈哈，怎么样，是不是看不懂？并且混淆包照常运行，没有任何问题。

[-------》源码地址《-------](http://download.csdn.net/download/rickiyeat/10260823)