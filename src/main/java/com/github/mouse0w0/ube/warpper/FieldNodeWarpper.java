package com.github.mouse0w0.ube.warpper;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class FieldNodeWarpper extends NodeWarpper<FieldNode, ClassNode>{

	public FieldNodeWarpper(FieldNode node, ClassNode parent) {
		super(node, parent);
	}

}