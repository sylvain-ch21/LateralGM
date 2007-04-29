package componentRes;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import mainRes.LGM;

public class GmTreeGraphics extends DefaultTreeCellRenderer
	{
	private ResNode last;
	private Icon[] kindIcons;
	private static final long serialVersionUID = 1L;

	public GmTreeGraphics()
		{
		super();
		setOpenIcon(LGM.findIcon("group_open.png"));
		setClosedIcon(LGM.findIcon("group.png"));
		setLeafIcon(getClosedIcon());
		kindIcons = new Icon[LGM.kinds.length];
		for (int i = 0; i < kindIcons.length; i++)
			kindIcons[i] = LGM.kinds[i] == "" ? null : LGM.findIcon(LGM.kinds[i] + ".png");
		}

	public Component getTreeCellRendererComponent(JTree tree, Object val, boolean sel, boolean exp,
			boolean leaf, int row, boolean focus)
		{
		last = (ResNode) val;
		super.getTreeCellRendererComponent(tree,val,sel,exp,leaf,row,focus);
		return this;
		}

	public Icon getLeafIcon()
		{
		if (last.status == ResNode.STATUS_SECONDARY) return kindIcons[last.kind];
		return getClosedIcon();
		}

	public Icon getNodeIcon(JTree tree, Object val, boolean sel, boolean exp, boolean leaf, int row)
		{
		ResNode node = (ResNode) val;
		if (leaf) if (node.status == ResNode.STATUS_SECONDARY) return kindIcons[node.kind];
		if (exp) return getOpenIcon();
		return getClosedIcon();
		}
	}