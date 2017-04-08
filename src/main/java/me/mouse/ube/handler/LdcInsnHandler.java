package me.mouse.ube.handler;

import org.objectweb.asm.Type;
import javafx.scene.control.TreeItem;
import me.mouse.ube.warpper.LdcInsnNodeWarpper;

public class LdcInsnHandler implements BytecodeHandler<LdcInsnNodeWarpper>{

	@Override
	public String getText(LdcInsnNodeWarpper item) {
		StringBuilder sb = new StringBuilder("LDC ");
		
        if (item.node.cst instanceof String) {
            sb.append("\"").append(item.node.cst).append("\"");
        } else if (item.node.cst instanceof Type) {
            sb.append(((Type) item.node.cst).getDescriptor()).append(".class");
        } else {
            sb.append(item.node.cst);
        }
        
		return sb.toString();
	}

	@Override
	public TreeItem<?> getNode(LdcInsnNodeWarpper node) {
		return new TreeItem<>(node);
	}

}
