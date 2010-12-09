package org.lateralgm.components.impl;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.image.BufferedImage;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.lateralgm.components.NumberField;
import org.lateralgm.components.visual.SpriteStripPreview;
import org.lateralgm.messages.Messages;

public class SpriteStripDialog extends JDialog
	{
	private static final long serialVersionUID = 1L;

	public BufferedImage img;
	public SpriteStripPreview preview;
	public NumberField fields[];

	public SpriteStripDialog(Frame owner, BufferedImage src)
		{
		super(owner,Messages.getString("StripDialog.TITLE"),true);

		img = src;

		String labels[] = { "IMAGE_NUMBER","IMAGES_PER_ROW","CELL_WIDTH","CELL_HEIGHT",
				"HOR_CELL_OFFSET","VERT_CELL_OFFSET","HOR_PIXEL_OFFSET","VERT_PIXEL_OFFSET","HOR_SEP",
				"VERT_SEP" };

		JLabel l[] = new JLabel[labels.length];
		fields = new NumberField[labels.length];

		JPanel p = new JPanel();
		GroupLayout layout = new GroupLayout(p);
		p.setLayout(layout);

		ParallelGroup g1 = layout.createParallelGroup();
		ParallelGroup g2 = layout.createParallelGroup();
		SequentialGroup g3 = layout.createSequentialGroup().addContainerGap();

		fields[0] = new NumberField(1,99999,1);
		fields[1] = new NumberField(1,99999,1);
		fields[2] = new NumberField(1,99999,32);
		fields[3] = new NumberField(1,99999,32);

		for (int i = 0; i < labels.length; i++)
			{
			l[i] = new JLabel(Messages.getString("StripDialog." + labels[i]));
			g1.addComponent(l[i]);
			if (i > 3) fields[i] = new NumberField(0);
			g2.addComponent(fields[i]);

			if ((i > 1 && i % 2 == 0) || i == 1)
				g3.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED);
			g3.addGroup(layout.createParallelGroup(Alignment.BASELINE)
			/**/.addComponent(l[i])
			/**/.addComponent(fields[i]));
			}

		preview = new SpriteStripPreview(this);

		layout.setHorizontalGroup(layout.createSequentialGroup()
		/**/.addContainerGap()
		/**/.addGroup(g1)
		/**/.addPreferredGap(ComponentPlacement.RELATED)
		/**/.addGroup(g2)
		/**/.addContainerGap());

		layout.setVerticalGroup(g3);

		add(p,BorderLayout.WEST);
		add(new JScrollPane(preview),BorderLayout.CENTER);

		pack();
		}

	public BufferedImage[] getStrip()
		{
		return null;
		}
	}