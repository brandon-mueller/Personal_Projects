package me.virusbrandon.agarlocalapis;

import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class ActionBarAPI{
  public static String nmsver;
  
  public ActionBarAPI(){
    nmsver = Bukkit.getServer().getClass().getPackage().getName();
    nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
  }
  
  enum ServerPackage {

	    MINECRAFT("net.minecraft.server." + getServerVersion()),
	    CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion());

	    private final String path;

	    ServerPackage(String path) {
	        this.path = path;
	    }

	    @Override
	    public String toString() {
	        return path;
	    }

	    public Class<?> getClass(String className) throws ClassNotFoundException {
	        return Class.forName(this.toString() + "." + className);
	    }

	    public static String getServerVersion() {
	        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
	    }

	}
  
	public void sendActionBar(Player player, String text) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player),
					connection = handle.getClass().getField("playerConnection").get(handle),
					component = ServerPackage.MINECRAFT.getClass("IChatBaseComponent$ChatSerializer")
							.getMethod("a", String.class).invoke(null, convert(text).toString()),
					packet = ServerPackage.MINECRAFT.getClass("PacketPlayOutChat")
							.getConstructor(ServerPackage.MINECRAFT.getClass("IChatBaseComponent"), byte.class)
							.newInstance(component, (byte) 2);
			connection.getClass().getMethod("sendPacket", ServerPackage.MINECRAFT.getClass("Packet")).invoke(connection,
					packet);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Convert Text To Json Format
	 * 
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static JSONObject convert(String text) {
        JSONObject json = new JSONObject();
        json.put("text", text);
        return json;
	}
	
	
	/**
	 * Returns Our Server Version
	 * 
	 * @return
	 */
	public String getNMSVer(){
		return (nmsver.replaceAll("_",".").substring(1,nmsver.length()).substring(0, nmsver.lastIndexOf("_")-1));
	}
}
