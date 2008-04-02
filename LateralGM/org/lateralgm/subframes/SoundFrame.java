/*
 * Copyright (C) 2007 IsmAvatar <cmagicj@nni.com>
 * Copyright (C) 2007 Quadduc <quadduc@gmail.com>
 * 
 * This file is part of LateralGM.
 * LateralGM is free software and comes with ABSOLUTELY NO WARRANTY.
 * See LICENSE for details.
 */

package org.lateralgm.subframes;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import org.lateralgm.compare.ResourceComparator;
import org.lateralgm.components.CustomFileChooser;
import org.lateralgm.components.impl.CustomFileFilter;
import org.lateralgm.components.impl.IndexButtonGroup;
import org.lateralgm.components.impl.ResNode;
import org.lateralgm.main.LGM;
import org.lateralgm.main.Util;
import org.lateralgm.messages.Messages;
import org.lateralgm.resources.Sound;

public class SoundFrame extends ResourceFrame<Sound>
	{
	private static final long serialVersionUID = 1L;
	private static final ImageIcon LOAD_ICON = LGM.getIconForKey("SoundFrame.LOAD"); //$NON-NLS-1$
	private static final ImageIcon PLAY_ICON = LGM.getIconForKey("SoundFrame.PLAY"); //$NON-NLS-1$
	private static final ImageIcon STOP_ICON = LGM.getIconForKey("SoundFrame.STOP"); //$NON-NLS-1$
	private static final ImageIcon STORE_ICON = LGM.getIconForKey("SoundFrame.STORE"); //$NON-NLS-1$
	private static final ImageIcon EDIT_ICON = LGM.getIconForKey("SoundFrame.EDIT"); //$NON-NLS-1$

	public JButton load;
	public JButton play;
	public JButton stop;
	public JButton store;
	public JLabel filename;
	public IndexButtonGroup kind;
	public IndexButtonGroup effects;
	public JSlider volume;
	public JSlider pan;
	public JCheckBox preload;
	public JButton edit;
	public byte[] data;
	public boolean modified = false;
	private CustomFileChooser fc = new CustomFileChooser("/org/lateralgm","LAST_SOUND_DIR");

	public SoundFrame(Sound res, ResNode node)
		{
		super(res,node);

		String s[] = { ".wav",".mid",".mp3" };
		String[] d = { Messages.getString("SoundFrame.FORMAT_SOUND"), //$NON-NLS-1$
				Messages.getString("SoundFrame.FORMAT_WAV"), //$NON-NLS-1$
				Messages.getString("SoundFrame.FORMAT_MID"), //$NON-NLS-1$
				Messages.getString("SoundFrame.FORMAT_MP3") }; //$NON-NLS-1$

		fc.setFileFilter(new CustomFileFilter(s,d[0]));
		fc.addChoosableFileFilter(new CustomFileFilter(s[0],d[1]));
		fc.addChoosableFileFilter(new CustomFileFilter(s[1],d[2]));
		fc.addChoosableFileFilter(new CustomFileFilter(s[2],d[3]));

		setSize(250,550);
		setResizable(false);
		setMaximizable(false);

		setContentPane(new JPanel());
		setLayout(new FlowLayout());

		JLabel label = new JLabel(Messages.getString("SoundFrame.NAME")); //$NON-NLS-1$
		label.setPreferredSize(new Dimension(40,14));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		add(label);
		name.setPreferredSize(new Dimension(180,20));
		add(name);

		load = new JButton(Messages.getString("SoundFrame.LOAD"),LOAD_ICON); //$NON-NLS-1$
		load.setPreferredSize(new Dimension(130,26));
		load.addActionListener(this);
		add(load);

		play = new JButton(PLAY_ICON);
		play.setPreferredSize(new Dimension(26,26));
		play.addActionListener(this);
		add(play);
		stop = new JButton(STOP_ICON);
		stop.setPreferredSize(new Dimension(26,26));
		stop.addActionListener(this);
		add(stop);

		addGap(40,1);
		store = new JButton(Messages.getString("SoundFrame.STORE"),STORE_ICON); //$NON-NLS-1$
		store.setPreferredSize(new Dimension(130,26));
		store.addActionListener(this);
		add(store);
		addGap(40,1);

		filename = new JLabel(Messages.getString("SoundFrame.FILE") + res.fileName); //$NON-NLS-1$
		filename.setPreferredSize(new Dimension(200,14));
		add(filename);

		kind = new IndexButtonGroup(4,true,false);
		AbstractButton b = new JRadioButton(Messages.getString("SoundFrame.NORMAL")); //$NON-NLS-1$
		b.setPreferredSize(new Dimension(170,14));
		kind.add(b,Sound.SOUND_NORMAL);
		b = new JRadioButton(Messages.getString("SoundFrame.BACKGROUND")); //$NON-NLS-1$
		b.setPreferredSize(new Dimension(170,14));
		kind.add(b,Sound.BACKGROUND);
		b = new JRadioButton(Messages.getString("SoundFrame.THREE")); //$NON-NLS-1$
		b.setPreferredSize(new Dimension(170,14));
		kind.add(b,Sound.SOUND_3D);
		b = new JRadioButton(Messages.getString("SoundFrame.MULT")); //$NON-NLS-1$
		b.setPreferredSize(new Dimension(170,14));
		kind.add(b,Sound.SOUND_MULTIMEDIA);
		kind.setValue(res.kind);
		JPanel p = Util.makeTitledPanel(Messages.getString("SoundFrame.KIND"),220,110); //$NON-NLS-1$
		kind.populate(p);
		add(p);

		// these are in bit order as appears in a GM6 file, not the same as GM shows them
		effects = new IndexButtonGroup(5,false);
		b = new JCheckBox(Messages.getString("SoundFrame.CHORUS")); //$NON-NLS-1$
		b.setPreferredSize(new Dimension(80,14));
		effects.add(b,1);
		b = new JCheckBox(Messages.getString("SoundFrame.ECHO")); //$NON-NLS-1$
		b.setPreferredSize(new Dimension(80,14));
		effects.add(b,2);
		b = new JCheckBox(Messages.getString("SoundFrame.FLANGER")); //$NON-NLS-1$
		b.setPreferredSize(new Dimension(80,14));
		effects.add(b,4);
		b = new JCheckBox(Messages.getString("SoundFrame.GARGLE")); //$NON-NLS-1$
		b.setPreferredSize(new Dimension(80,14));
		effects.add(b,8);
		b = new JCheckBox(Messages.getString("SoundFrame.REVERB")); //$NON-NLS-1$
		b.setPreferredSize(new Dimension(80,14));
		effects.add(b,16);
		effects.setValue(res.getEffects());
		p = Util.makeTitledPanel(Messages.getString("SoundFrame.EFFECTS"),220,90); //$NON-NLS-1$
		effects.populate(p);
		add(p);

		label = new JLabel(Messages.getString("SoundFrame.VOLUME")); //$NON-NLS-1$
		label.setPreferredSize(new Dimension(60,14));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		add(label);
		volume = new JSlider(0,100,100);
		volume.setMajorTickSpacing(10);
		volume.setPaintTicks(true);
		volume.setValue((int) (res.volume * 100));
		add(volume);

		label = new JLabel(Messages.getString("SoundFrame.PAN")); //$NON-NLS-1$
		label.setPreferredSize(new Dimension(40,14));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		add(label);
		pan = new JSlider(-100,100,0);
		pan.setMajorTickSpacing(20);
		pan.setPaintTicks(true);
		pan.setValue((int) (res.pan * 100));
		add(pan);

		preload = new JCheckBox(Messages.getString("SoundFrame.PRELOAD"),res.preload); //$NON-NLS-1$
		preload.setPreferredSize(new Dimension(200,20));
		preload.setSelected(res.preload);
		add(preload);

		addGap(50,1);
		edit = new JButton(Messages.getString("SoundFrame.EDIT"),EDIT_ICON); //$NON-NLS-1$
		edit.addActionListener(this);
		add(edit);
		addGap(50,1);

		save.setPreferredSize(new Dimension(100,27));
		save.setText(Messages.getString("SoundFrame.SAVE")); //$NON-NLS-1$
		save.setAlignmentX(0.5f);
		add(save);

		data = res.data;
		}

	public boolean resourceChanged()
		{
		commitChanges();
		if (modified) return true;
		ResourceComparator c = new ResourceComparator();
		c.addExclusions(Sound.class,"data"); //$NON-NLS-1$
		return !c.areEqual(res,resOriginal);
		}

	public void revertResource()
		{
		LGM.currentFile.sounds.replace(res,resOriginal);
		}

	public void commitChanges()
		{
		res.setName(name.getText());

		String n = filename.getText().substring(Messages.getString("SoundFrame.FILE").length()); //$NON-NLS-1$
		res.fileName = n;
		res.fileType = CustomFileFilter.getExtension(n);
		if (res.fileType == null) res.fileType = "";
		res.kind = kind.getValue();
		res.setEffects(effects.getValue());
		res.volume = volume.getValue() / 100.0;
		res.pan = pan.getValue() / 100.0;
		res.preload = preload.isSelected();
		res.data = data;
		}

	public void updateResource()
		{
		super.updateResource();
		modified = false;
		}

	public void actionPerformed(ActionEvent e)
		{
		if (e.getSource() == load)
			{
			while (true)
				{
				if (fc.showOpenDialog(LGM.frame) != JFileChooser.APPROVE_OPTION) return;
				if (fc.getSelectedFile().exists()) break;
				JOptionPane.showMessageDialog(null,fc.getSelectedFile().getName()
						+ Messages.getString("SoundFrame.FILE_MISSING"), //$NON-NLS-1$
						Messages.getString("SoundFrame.FILE_OPEN"),JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
				}
			try
				{
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(fc.getSelectedFile()));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int val = in.read();
				while (val != -1)
					{
					out.write(val);
					val = in.read();
					}
				data = out.toByteArray();
				out.close();
				in.close();
				}
			catch (Exception ex)
				{
				ex.printStackTrace();
				}
			modified = true;
			return;
			}
		if (e.getSource() == play)
			{
			return;
			}
		if (e.getSource() == stop)
			{
			return;
			}
		if (e.getSource() == store)
			{
			if (fc.showSaveDialog(LGM.frame) != JFileChooser.APPROVE_OPTION) return;
			try
				{
				ByteArrayInputStream in = new ByteArrayInputStream(data);
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
						fc.getSelectedFile()));
				int val = in.read();
				while (val != -1)
					{
					out.write(val);
					val = in.read();
					}
				out.close();
				in.close();
				}
			catch (Exception ex)
				{
				ex.printStackTrace();
				}
			return;
			}
		if (e.getSource() == edit)
			{
			return;
			}
		super.actionPerformed(e);
		}
	}
