
v1.1

�����֧��bukkit��spigot

v1.0

���ʹ��maven��д��

�������pluginĿ¼�£�

�����ǽ̡̳�



�ο���

https://github.com/InfinityStudio/Bukkit-Wiki-Chinese-Translation/blob/master/Developers/Plugin_Tutorial.md


˵����

���µ�maven pom������repo.bukkit.org���ɷ��ʣ��ʽ����Ϊʹ��spigot�����maven��������

�������漰�������ʹ��ǰ��ע��ʹ��

OP��ĳ�������������Ȩ��

���������Դ

https://github.com/Bukkit/SamplePlugin

v1.1

��ɺ�����

git@github.com:pan2za/EssentiallyMisc.git
https://github.com/pan2za/EssentiallyMisc
�ҵ��޸ĺ���ֲ�ǽ���bukkit֧�ִ�1.7.2��Ϊ֧��spigot 1.11(����֤��


OS��win7

Ԥװ��eclipse, jdk8��git�� tortoisegit

��1.7.2�汾��ʹ��jdk7, ��1.11�汾��Ҫʹ��jdk8. ����Ҫ֧��String.join�����ķ�����


��һ���ֽ�������ش��뵽����


������ӵ������Ǵ�����/pos�������ȷ���û�λ�ã����͵�ָ��λ��

���ش���

ʹ��tortoisegit���Ҽ��˵���ѡ��git clone


urlѡ�� https://github.com/pan2za/EssentiallyMisc

��v1.7.2�� https://github.com/Bukkit/SamplePlugin��Ŀǰ������ʹ�ã���repo.bukkit��ʱ�޷����ʣ������޷����룩


������ɺ���eclipse��Ŀ

Ԥװ��eclipse��jdk1.8�����Ƽ�1.7��ԭ������ƪ��

����ʹ��maven

���裺

1��eclipse

2ѡ��˵��ļ�-������

3 ������������maven��ѡ����maven��Ŀ

4 �����غ�ı���EssentiallyMiscĿ¼��ѡ��pom.xml

����ļ�����maven��Ŀ

���ϵ���ɹ�

5 ��eclipse�������Ŀ��ѡ��EssentiallyMiscĿ¼��

6 �Ҽ�run as-��maven build������

7 ���ñ�����������ڣ�

��������Ϊcompile

jdkѡ��Ϊjdk1.8������jre1.8,������jdk1.7)

8 ѡ����룬������class�ļ�

���ϱ���mvn compile

9 �ظ�����6

10 ���ñ������������

��������Ϊpackage

jdkͬ7

11 ѡ����룬������jar��

��������jar������Ӧmvn package

12 ��jar��������craftbukkit����Ŀ¼��pluginĿ¼�¡�

���Ͻ��������

�����ҵ����������������reload����ʱ����ʾ�����ʹ��

�����ҵ�����ͻ��˻�����������ʱ����/pos�����ø���λ�á�Զ��ӦҲû����

���ŷ���һ��/get�����������/give��/get x y������û�y��x���

ԭ��������

��ʼ������

[java] view plain copy
super(plugin, "get", 1, 2);  
�������ٰ���1�����������2��

doPlayerCommand����BaseCommand��ʵ��

�߼����£��������������2��������times������

���x����������֣���ô�Ͱ���������ֵ�ȫ���Ӹ��û�

[java] view plain copy ��CODE�ϲ鿴����Ƭ�������ҵĴ���Ƭ
if (c.toString().toLowerCase().contains(cur.toLowerCase()))  
����/get diamond 20�ͻ��������שʯ�Ķ����û�������diamond_boots, diamond_pickaxe
���x����������֣���ô�Ͱѷ������idҪ��ĸ��û�

����/get 262 2�ͻ������id��262��������û���


�������£�


[java] view plain copy ��CODE�ϲ鿴����Ƭ�������ҵĴ���Ƭ
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
