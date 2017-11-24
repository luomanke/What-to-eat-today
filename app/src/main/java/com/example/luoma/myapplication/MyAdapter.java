package com.example.luoma.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

/**
 * Created by LuoMa on 10/10/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    //our items
    private List mDataset;
    private static final int FOOTER_VIEW = 1;
    OnItemSelectListener mOnItemSelectListener;

    public MyAdapter(List myDataset) {
        mDataset = myDataset;
    }

    public void setOnItemSelectListener(OnItemSelectListener mListener){
        this.mOnItemSelectListener = mListener;
    }

    // Define a view holder for Footer view
    public class FooterViewHolder extends ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
            mButton = (Button) itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do whatever you want on clicking the item
                    mOnItemSelectListener.OnAddBtnSelect();
                }
            });
        }
    }
    // Now define the viewholder for Normal list item
    public class NormalViewHolder extends ViewHolder {
        public NormalViewHolder(View itemView) {
            super(itemView);
            mSwipeView = (SwipeLayout) itemView;
            BlurLayout sampleLayout = (BlurLayout)mSwipeView.findViewById(R.id.sample);
            View hover = LayoutInflater.from(itemView.getContext()).inflate(R.layout.hover_layout,null);
            sampleLayout.setHoverView(hover);
            sampleLayout.addChildAppearAnimator(hover, R.id.delete, Techniques.FlipInX);
            sampleLayout.addChildDisappearAnimator(hover, R.id.delete, Techniques.FlipOutX);

        }
    }

    // And now in onCreateViewHolder you have to pass the correct view
    // while populating the list item.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        ViewHolder vh;
        if (viewType == FOOTER_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_item, parent, false);
            vh = new FooterViewHolder(v);
        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
            vh = new NormalViewHolder(v);
        }
        return vh;
    }

    // Now bind the viewholders in onBindViewHolder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder vh = (NormalViewHolder) holder;

                TextView t = (TextView) vh.mSwipeView.findViewById(R.id.position);
                t.setText(mDataset.get(position).toString());

                Button goButton = (Button) vh.mSwipeView.findViewById(R.id.go);
                goButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
//                        notifyDataSetChanged();
                        mOnItemSelectListener.onGoBtnSelect(position);
                    }
                });

                Button delButton = (Button) vh.mSwipeView.findViewById(R.id.delete);
                delButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        mOnItemSelectListener.onDelBtnSelect(position);
                        mDataset.remove(position);
                        notifyItemRemoved(position);
                    }
                });

                vh.bindView(position,vh);
            }
//            else if (holder instanceof FooterViewHolder) {
//                // FooterViewHolder vh = (FooterViewHolder) holder;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Now the critical part. You have return the exact item count of your list
    // I've only one footer. So I returned data.size() + 1
    // If you've multiple headers and footers, you've to return total count
    // like, headers.size() + data.size() + footers.size()
    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        }
        if (mDataset.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }
        // Add extra view to show the footer view
        return mDataset.size() + 1;
    }

    // Now define getItemViewType of your own.
    @Override
    public int getItemViewType(int position) {
        if (position == mDataset.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }

    // So you're done with adding a footer and its action on onClick.
    // Now set the default ViewHolder for NormalViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define elements of a row here
        public SwipeLayout mSwipeView;
        public Button mButton;
        public ViewHolder(View itemView) {
            super(itemView);
            // Find view by ID and initialize here
        }

        public void bindView(int position, ViewHolder vh) {
            // bindView() method to implement actions
        }
    }
}