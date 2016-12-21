package me.virusbrandon.Micro_SG;

public class BlockNode implements java.io.Serializable{
	private static final long serialVersionUID = 19789048L;
	
	TBlock data;
	BlockNode link;
	public BlockNode(TBlock initialData, BlockNode link){
		this.data= initialData;
		this.link = link;
	}
	
	public BlockNode addNodeAfter(TBlock block){
		link = new BlockNode(block, link);
		return link;
	}
	
	public static int listLength(BlockNode head){
		int count = 0;
		for(;head.link != null;){
			count++;
			head = head.link;
		}
		return count;
	}
	
	public TBlock getTBlock(){
		return data;
	}
}