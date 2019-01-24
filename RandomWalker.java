package custom_agents;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import negotiator.Agent;
import negotiator.Bid;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.ActionWithBid;
import negotiator.actions.Offer;
import negotiator.issue.Issue;
import negotiator.issue.IssueDiscrete;
import negotiator.issue.IssueReal;
import negotiator.issue.Value;
import negotiator.issue.ValueReal;

public class RandomWalker extends Agent{
	private static final long serialVersionUID = 1L;
	private Random rand;
	private Bid lastBid;
	
	@Override
	public void init(){
		super.init();
		rand = new Random();
	}
	@Override
	public Action chooseAction() {
	Bid bid = utilitySpace.getDomain().getRandomBid(rand);
	Action action = new Offer(getAgentID(), bid);
	return action;
	}
public void ReceiveMessage(Action opponentAction){
if(action instanceof Offer)
lastBid = ((Offer) opponentAction).getBid();
}


public Action chooseAction() {
Action action = null;
try {
if (actionOfPartner == null) {
action = chooseRandomBidAction();
}
if (actionOfPartner instanceof Offer) {
Bid partnerBid = ((Offer) actionOfPartner).getBid();
double offeredUtilFromOpponent = getUtility(partnerBid);
// get current time
double time = timeline.getTime();
action = chooseRandomBidAction();
Bid myBid = ((Offer) action).getBid();
double myOfferedUtil = getUtility(myBid);
// accept under certain circumstances
if (isAcceptable(offeredUtilFromOpponent,myOfferedUtil,time)) {
action = new Accept(getAgentID());
}
}
} catch (Exception e) {
e.printStackTrace();
action = new Accept(getAgentID()); // best guess if things go wrong.
}
return action;
}

}

