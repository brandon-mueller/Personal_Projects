package me.virusbrandon.localapis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarAPI{
  public static String nmsver;
  
  public ActionBarAPI(){
    nmsver = Bukkit.getServer().getClass().getPackage().getName();
    nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
  }
  
  public void sendActionBar(Player player, String message){
    try{
      Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
      Object p = c1.cast(player);
      Object ppoc = null;
      Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
      Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
	  if (!(!nmsver.equalsIgnoreCase("v1_8_R1") && nmsver.startsWith("v1_8_") || (nmsver.startsWith("v1_9_")|nmsver.startsWith("v1_10_")))) {
    	Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
        Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
        Method m3 = c2.getDeclaredMethod("a", new Class[] { String.class });
        Object cbc = c3.cast(m3.invoke(c2, new Object[] { "{\"text\": \"" + message + "\"}" }));
        ppoc = c4.getConstructor(new Class[] { c3, Byte.TYPE }).newInstance(new Object[] { cbc, Byte.valueOf((byte) 2) });
      }else{
        Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
        Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
        Object o = c2.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
        ppoc = c4.getConstructor(new Class[] { c3, Byte.TYPE }).newInstance(new Object[] { o, Byte.valueOf((byte)2) });
      }
      Method m1 = c1.getDeclaredMethod("getHandle", new Class[0]);
      Object h = m1.invoke(p, new Object[0]);
      Field f1 = h.getClass().getDeclaredField("playerConnection");
      Object pc = f1.get(h);
      Method m5 = pc.getClass().getDeclaredMethod("sendPacket", new Class[] { c5 });
      m5.invoke(pc, new Object[] { ppoc });
    }catch (Exception ex){}
  }
  
  /*
   * � 2015 Brandon Mueller
   * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
   */
}
