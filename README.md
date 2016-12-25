
v1.1

插件可支持bukkit和spigot

v1.0

插件使用maven编写，

插件放在plugin目录下，

下面是教程。



参考：

https://github.com/InfinityStudio/Bukkit-Wiki-Chinese-Translation/blob/master/Developers/Plugin_Tutorial.md


说明：

最新的maven pom中由于repo.bukkit.org不可访问，故建议改为使用spigot库进行maven依赖处理

本案例涉及命令，所以使用前：注意使用

OP给某个玩家所有命令权限

插件例子来源

https://github.com/Bukkit/SamplePlugin

v1.1

完成后例子

git@github.com:pan2za/EssentiallyMisc.git
https://github.com/pan2za/EssentiallyMisc
我的修改和移植是将对bukkit支持从1.7.2改为支持spigot 1.11(已验证）


OS：win7

预装：eclipse, jdk8，git， tortoisegit

（1.7.2版本可使用jdk7, 但1.11版本需要使用jdk8. 因需要支持String.join这样的方法）


第一部分讲如何下载代码到本地


这个例子的作用是创建了/pos命令，用于确定用户位置，或传送到指定位置

下载代码

使用tortoisegit，右键菜单，选择git clone


url选择 https://github.com/pan2za/EssentiallyMisc

（v1.7.2是 https://github.com/Bukkit/SamplePlugin，目前不建议使用，因repo.bukkit暂时无法访问，导致无法编译）


下载完成后建立eclipse项目

预装：eclipse，jdk1.8（不推荐1.7，原因详上篇）

编译使用maven

步骤：

1打开eclipse

2选择菜单文件-》导入

3 在搜索中输入maven，选择导入maven项目

4 从下载后的本地EssentiallyMisc目录中选中pom.xml

这个文件就是maven项目

以上导入成功

5 在eclipse左侧栏项目中选中EssentiallyMisc目录，

6 右键run as-》maven build。。。

7 设置编译的生命周期，

本次设置为compile

jdk选择为jdk1.8（不是jre1.8,更不是jdk1.7)

8 选择编译，将生成class文件

以上编译mvn compile

9 重复步骤6

10 设置编译的生命周期

本次设置为package

jdk同7

11 选择编译，将生成jar包

以上生成jar包，对应mvn package

12 将jar包拷贝到craftbukkit所在目录的plugin目录下。

以上将插件放入

运行我的世界服务器，或者reload，此时会提示插件被使能

运行我的世界客户端或启动器，此时输入/pos命令将获得个人位置。远传应也没问题

试着分析一下/get命令，命令类似/give，/get x y代表给用户y个x物件

原代码如下

初始化函数

[java] view plain copy
super(plugin, "get", 1, 2);  
其中最少包含1个参数，最多2个

doPlayerCommand来自BaseCommand的实现

逻辑如下，如果参数长度是2，将倍数times存起来

如果x输入的是名字，那么就把有这个名字的全都加给用户

[java] view plain copy 在CODE上查看代码片派生到我的代码片
if (c.toString().toLowerCase().contains(cur.toLowerCase()))  
例如/get diamond 20就会把所有是砖石的都给用户，包括diamond_boots, diamond_pickaxe
如果x输入的是数字，那么就把符合这个id要求的给用户

例如/get 262 2就会把所有id是262的物件给用户。


代码如下，


[java] view plain copy 在CODE上查看代码片派生到我的代码片
public class CmdGetblock extends BaseCommand {  
  
    Map<String, ConfigurationSection> allKits;  
  
    public CmdGetblock(MainPlugin plugin) {  
        super(plugin, "get", 1, 2);  
    }  
  
    @Override  
    @SuppressWarnings("deprecation")  
    protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] split) {  
          
  
          Integer times = Integer.valueOf(1);  
          if (split.length == 2)  
          {  
            try  
            {  
              times = Integer.valueOf(Integer.parseInt(split[1]));  
            }  
            catch (Exception localException) {}  
            if (times.intValue() < 0) {  
              times = Integer.valueOf(1);  
            }  
          }  
          if ((split.length == 1) || (split.length == 2))  
          {  
            String cur = split[0];  
            boolean found = false;  
            Material[] arrayOfMaterial;  
            int j = (arrayOfMaterial = Material.values()).length;  
  
            for (int ai = 0; ai < j; ai++)  
            {  
              Material c = arrayOfMaterial[ai];  
              if (c.toString().toLowerCase().contains(cur.toLowerCase()))  
              {  
                found = true;  
                for (int ki = 0; ki < times.intValue(); ki++) {  
                  player.getInventory().addItem(new ItemStack[] { new ItemStack(c.getId(), 1) });  
                }  
                player.sendMessage("Material added maybe: " + c.toString() + " with id " + String.valueOf(c.getId()));  
              }  
            }  
            if (!found) {  
              try  
              {  
                Integer x = Integer.valueOf(Integer.parseInt(split[0]));  
                Material[] all = Material.values();  
                int k = all.length;  
                for (j = 0; j < k; j++)  
                {  
                  Material c = all[j];  
                  if (c.getId() == x.intValue())  
                  {  
                    found = true;  
                    for (int i = 0; i < times.intValue(); i++) {  
                      player.getInventory().addItem(new ItemStack[] { new ItemStack(c.getId(), 1) });  
                    }  
                    player.sendMessage("Material added maybe: " + c.toString() + " with id " + String.valueOf(c.getId()));  
                  }  
                }  
              }  
              catch (Exception localException1) {}  
            }  
            return true;  
          }  
          return false;  
    }  
}  
