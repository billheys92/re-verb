package feed;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.re.reverb.R;

public class FeedExpandListView extends Fragment
{
	    //Initialize variables
	    private ArrayList<Message> parents;
	    
	    private FeedListViewAdapter feedListViewAdapter;
	     
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View theView = inflater.inflate(R.layout.fragment_main_feed, container, false);
			
			ExpandableListView elv = (ExpandableListView) theView.findViewById(R.id.feedListView);
		    
	        final ArrayList<Message> dummyList = buildDummyData();
	        
	        loadHosts(dummyList);
			
		    elv.setAdapter(feedListViewAdapter);
	        
			return theView;
		}
	     
	    /**
	     * here should come your data service implementation
	     * @return
	     */
	    private ArrayList<Message> buildDummyData()
	    {
	        // Creating ArrayList of type parent class to store parent class objects
	        final ArrayList<Message> list = new ArrayList<Message>();
	        for (int i = 1; i < 4; i++)
	        {
	            //Create parent class object
	            final Message parent = new Message();
	             
	              // Set values in parent class object
	                  if(i==1){
	                	  System.out.println("Created DATA!");
	                      parent.setUserName("" + i);
	                      parent.setHandle("Parent 0");
	                      parent.setBody("Disable App On \nBattery Low");
	                      parent.setChildren(new ArrayList<MessageReply>());
	                       
	                      // Create Child class object 
	                      final MessageReply child = new MessageReply();
	                        child.setUserName("" + i);
	                        child.setHandle("Child 0");
	                        child.setBody("Body Text");
	                         
	                        //Add Child class object to parent class object
	                        parent.getChildren().add(child);
	                    }
	                   else if(i==2){
	                       parent.setUserName("" + i);
	                       parent.setHandle("Parent 1");
	                       parent.setBody("Auto disable/enable App \n at specified time");
	                       parent.setChildren(new ArrayList<MessageReply>());
	                        
	                       final MessageReply child = new MessageReply();
	                        child.setUserName("" + i);
	                        child.setHandle("Child 0");
	                        child.setBody("Body Text");
	                        parent.getChildren().add(child);
	                       final MessageReply child1 = new MessageReply();
	                        child1.setUserName("" + i);
	                        child1.setHandle("Child 1");
	                        child.setBody("Body Text");
	                        parent.getChildren().add(child1);
	                     }
	                   else if(i==3){
	                       parent.setUserName("" + i);
	                       parent.setHandle("Parent 1");
	                       parent.setBody("Show App Icon on \nnotification bar");
	                       parent.setChildren(new ArrayList<MessageReply>());
	                        
	                       final MessageReply child = new MessageReply();
	                        child.setUserName("" + i);
	                        child.setHandle("Child 0");
	                        child.setBody("Body Text");
	                        parent.getChildren().add(child);
	                       final MessageReply child1 = new MessageReply();
	                        child1.setUserName("" + i);
	                        child1.setHandle("Child 1");
	                        child.setBody("Body Text");
	                        parent.getChildren().add(child1);
	                      final MessageReply child2 = new MessageReply();
	                        child2.setUserName("" + i);
	                        child2.setHandle("Child 2");
	                        child.setBody("Body Text");
	                        parent.getChildren().add(child2);
	                       final MessageReply child3 = new MessageReply();
	                        child3.setUserName("" + i);
	                        child3.setHandle("Child 3");
	                        child.setBody("Body Text");
	                        parent.getChildren().add(child3);
	                      }
	             
	            //Adding Parent class object to ArrayList         
	            list.add(parent);
	        }
	        return list;
	    }
	     
	     
	    private void loadHosts(final ArrayList<Message> newParents)
	    {
	        if (newParents == null)
	            return;
	         
	        parents = newParents;
	         
	        // Check for ExpandableListAdapter object
	        if (this.getFeedListViewAdapter() == null)
	        {
	             //Create ExpandableListAdapter Object
	            final FeedListViewAdapter mAdapter = new FeedListViewAdapter(this.getActivity(), this.parents);
	             
	            // Set Adapter to ExpandableList Adapter
	            this.setFeedListViewAdapter(mAdapter);
	        }
	        else
	        {
	             // Refresh ExpandableListView data 
	            ((FeedListViewAdapter)getFeedListViewAdapter()).notifyDataSetChanged();
	        }   
	    }
	    
	    public FeedListViewAdapter getFeedListViewAdapter()
	    {
	    	return this.feedListViewAdapter;
	    }
	    
	    public void setFeedListViewAdapter(FeedListViewAdapter adapter)
	    {
	    	this.feedListViewAdapter = adapter;
	    }
	    
	    
}