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

public class RandomWalker extends Agent {
	private static final long serialVersionUID = 1L;
	private Random rand;
	private Bid lastBid;

	@Override
	public void init() {
		super.init();
		rand = new Random();
	}

	@Override
	public Action chooseAction() {
		Bid bid = utilitySpace.getDomain().getRandomBid(rand);
		Action action = new Offer(getAgentID(), bid);
		return action;
	}

	public Action chooseAction1() {
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
				if (isAcceptable(offeredUtilFromOpponent, myOfferedUtil, time)) {
					action = new Accept(getAgentID());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			action = new Accept(getAgentID()); // best guess if things go wrong.
		}
		return action;
	}

	public void ReceiveMessage(Action opponentAction) {
		if (opponentAction instanceof Offer)
			lastBid = ((Offer) opponentAction).getBid();
	}
	
	private boolean isAcceptable(double offeredUtilFromOpponent, double myOfferedUtil, double time) throws Exception {
		double P = Paccept(offeredUtilFromOpponent, time);
		if (P > Math.random())
			return true;
		return false;
	}
	
	/**
	 * This function determines the accept probability for an offer. At t=0 it
	 * will prefer high-utility offers. As t gets closer to 1, it will accept
	 * lower utility offers with increasing probability. it will never accept
	 * offers with utility 0.
	 * 
	 * @param u
	 *            is the utility
	 * @param t
	 *            is the time as fraction of the total available time (t=0 at
	 *            start, and t=1 at end time)
	 * @return the probability of an accept at time t
	 * @throws Exception
	 *             if you use wrong values for u or t.
	 * 
	 */
	double Paccept(double u, double t1) throws Exception {
		double t = t1 * t1 * t1; // steeper increase when deadline approaches.
		if (u < 0 || u > 1.05)
			throw new Exception("utility " + u + " outside [0,1]");
		// normalization may be slightly off, therefore we have a broad boundary
		// up to 1.05
		if (t < 0 || t > 1)
			throw new Exception("time " + t + " outside [0,1]");
		if (u > 1.)
			u = 1;
		if (t == 0.5)
			return u;
		return (u - 2. * u * t + 2. * (-1. + t + Math.sqrt(sq(-1. + t) + u * (-1. + 2 * t)))) / (-1. + 2 * t);
	}
	
	double sq(double x) {
		return x * x;
	}
}
