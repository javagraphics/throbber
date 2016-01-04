/*
 * @(#)ChasingArrowsThrobberUI.java
 *
 * $Date: 2014-11-27 07:55:25 +0100 (Do, 27 Nov 2014) $
 *
 * Copyright (c) 2014 by Jeremy Wood.
 * All rights reserved.
 *
 * The copyright of this software is owned by Jeremy Wood. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Jeremy Wood. For details see accompanying license terms.
 * 
 * This software is probably, but not necessarily, discussed here:
 * https://javagraphics.java.net/
 * 
 * That site should also contain the most recent official version
 * of this software.  (See the SVN repository for more details.)
 */
package com.bric.plaf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

/** A <code>ThrobberUI</code> of two arrows that rotate clockwise.
 * <p><table summary="Sample Animations of ChasingArrowsThrobberUI" cellpadding="10"><tr>
 * <td><img src="https://javagraphics.java.net/resources/throbber/ChasingArrowsThrobberUI.gif" alt="ChassingArrowsThrobberUI"></td>
 * <td><img src="https://javagraphics.java.net/resources/throbber/ChasingArrowsThrobberUIx2.gif" alt="ChassingArrowsThrobberUI Magnified 2x"></td>
 * <td><img src="https://javagraphics.java.net/resources/throbber/ChasingArrowsThrobberUIx4.gif" alt="ChassingArrowsThrobberUI Magnified 4x"></td>
 * </tr></table>
 * <p>On installation: the component's foreground is set to dark gray,
 * but if that is changed then that color is used to render this animation.
 * <P>The default period for this animation is 2000, but you can modify
 * this with the period client properties {@link ThrobberUI#PERIOD_KEY} or
 * {@link ThrobberUI#PERIOD_MULTIPLIER_KEY}.
 */
public class ChasingArrowsThrobberUI extends ThrobberUI {

	/** The default duration (in ms) it takes to complete a cycle.
	 */
	public static final int DEFAULT_PERIOD = 2000;
	
	private static final float PI = (float)Math.PI;
	private static final int[] x = new int[] {8, 8, 11};
	private static final int[] y = new int[] {0, 6, 3};
	private static final BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

	private AffineTransform transform = new AffineTransform();
	private Arc2D arc = new Arc2D.Float(3, 3, 10, 10, 65, 140, Arc2D.OPEN);
	private GeneralPath path = new GeneralPath();
	
	public ChasingArrowsThrobberUI() {
		super(1000/24);
	}
	
	@Override
	protected synchronized void paintForeground(Graphics2D g,JComponent jc,Dimension size,Float fixedFraction) {
		
		g.setStroke(stroke);

		float f;
		if(fixedFraction!=null) {
			f = fixedFraction;
		} else {
			int p = getPeriod(jc, DEFAULT_PERIOD);
			f = System.currentTimeMillis()%p;
			f = f/((float)p);
		}
		f = f * 2 * PI;
		
		Color color = jc==null ? getDefaultForeground() : jc.getForeground();
		g.setColor(color);

		for(int k = 0; k<2; k++) {
			transform.setToRotation(f+ k*Math.PI, size.width/2, size.height/2);
	
			path.reset();
			path.moveTo(x[0],y[0]);
			path.lineTo(x[1],y[1]);
			path.lineTo(x[2],y[2]);
			path.lineTo(x[0],y[0]);
			path.transform(transform);
			
			g.fill(path);
			
			path.reset();
			path.append(arc.getPathIterator(transform), false);
			g.draw(path);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(16, 16);
	}
	
	@Override
	public Color getDefaultForeground() {
		return Color.darkGray;
	}
}
