package Scripts.TestMap;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import GameObject.Frame;
import Level.*;
import Maps.TestMap;
import Utils.Direction;
import Utils.Point;

// script for talking to dino npc
// the script is segmented -- it has multiple setups, cleanups, and executions based on its current action
public class DinoScriptItemGive extends Script<NPC> {
	
	@Override
	protected void setup() {
		lockPlayer();
		showTextbox();
		
		
		if (!isFlagSet("hasGivenStaffItem") && !isFlagSet("hasGivenStaffItem")) {
			addTextToTextboxQueue("Have a staff!");
			
			

		} else if (isFlagSet("hasGivenStaffItem")) {
			addTextToTextboxQueue("Gimme your staff!");
		}
		entity.facePlayer(player);

	}

	  @Override
	    protected void cleanup() {
	        unlockPlayer();
	        hideTextbox();

	        if (!isFlagSet("hasGivenSwordItem") && !isFlagSet("hasGivenStaffItem")) {
				setFlag("hasGivenStaffItem");
				setFlag("discoveredStaff");
				//unsetFlag("hasDroppedSword");
			} else if (isFlagSet("hasGivenStaffItem")) {
				unsetFlag("hasGivenSwordItem");
				unsetFlag("hasGivenStaffItem");
				unsetFlag("discoveredSword");
				//setFlag("hasDroppedSword");
			}
	    }

	    @Override
	    public ScriptState execute() {
	        start();
	        if (!isTextboxQueueEmpty()) {
	            return ScriptState.RUNNING;
	        }
	        end();
	        return ScriptState.COMPLETED;
	    }
}

