package com.example.luoma.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LuoMa on 10/10/2017.
 */

class MainActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    //our items
    private List mTitles;
    private List mRests;
    private static final int FOOTER_VIEW = 1;
    private OnItemSelectListener mOnItemSelectListener;


    MainActivityAdapter(List titles, List rests) {
        mTitles = titles;
        mRests = rests;
    }

    void setOnItemSelectListener(OnItemSelectListener mListener){
        this.mOnItemSelectListener = mListener;
    }

    // Define a view holder for Footer view
    public class FooterViewHolder extends ViewHolder {
        FooterViewHolder(View itemView) {
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
        SwipeLayout mSwipeLayout;
        BlurLayout mBlurLayout;

        NormalViewHolder(View itemView) {
            super(itemView);
            mSwipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            mBlurLayout = (BlurLayout) itemView.findViewById(R.id.hover_layout);
        }
    }

    // And now in onCreateViewHolder you have to pass the correct view
    // while populating the list item.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        ViewHolder vh;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == FOOTER_VIEW) {
            v = inflater.inflate(R.layout.footer_item, parent, false);
            vh = new FooterViewHolder(v);
        }
        else {
            v = inflater.inflate(R.layout.recyclerview_item, parent, false);
            BlurLayout mBlurLayout = (BlurLayout) LayoutInflater.from(v.getContext()).inflate(R.layout.hover_layout, (ViewGroup) v, true);
            vh = new NormalViewHolder(v);
        }
        return vh;
    }

    // Now bind the viewholders in onBindViewHolder
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder vh = (NormalViewHolder) holder;

                TextView t = (TextView) vh.mSwipeView.findViewById(R.id.position);
                t.setText(mTitles.get(position).toString());

                // BlurLayout in HoverLayout
                BlurLayout mBlurLayout = (BlurLayout)vh.mSwipeView.findViewById(R.id.blurLayout_baseLayer);
                View hover = LayoutInflater.from(vh.mSwipeView.getContext()).inflate(R.layout.hover_layout,null);
                mBlurLayout.setHoverView(hover);

                // GridView in HoverLayout
                mBlurLayout.addChildAppearAnimator(hover, R.id.rests_gv, Techniques.FadeInLeft);
                mBlurLayout.addChildDisappearAnimator(hover, R.id.rests_gv, Techniques.FadeOutLeft);
                GridView rest_gv = (GridView) hover.findViewById(R.id.rests_gv);
                String temp = mRests.get(position).toString().replaceAll("[\\[\\]\"]","");
                String[] rests = temp.split(", ");
                ArrayList<String> restList = new ArrayList<String>();
                restList.addAll(Arrays.asList(rests));
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(hover.getContext(), R.layout.hover_gridviewitem, restList);
                rest_gv.setAdapter( listAdapter );


                // Button go in HoverLayout
                mBlurLayout.addChildAppearAnimator(hover, R.id.go, Techniques.SlideInRight);
                mBlurLayout.addChildDisappearAnimator(hover, R.id.go, Techniques.SlideOutRight);
                Button goButton = (Button) hover.findViewById(R.id.go);
                goButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        mOnItemSelectListener.onGoBtnSelect(holder.getAdapterPosition());
                    }
                });


                // Button delete in SwipeLayout
                Button delButton = (Button) vh.mSwipeView.findViewById(R.id.delete);
                delButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        mOnItemSelectListener.onDelBtnSelect(holder.getAdapterPosition());
                        mTitles.remove(holder.getAdapterPosition());
                        mRests.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
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
        if (mTitles == null) {
            return 0;
        }
        if (mTitles.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }
        // Add extra view to show the footer view
        return mTitles.size() + 1;
    }

    // Now define getItemViewType of your own.
    @Override
    public int getItemViewType(int position) {
        if (position == mTitles.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }

    // So you're done with adding a footer and its action on onClick.
    // Now set the default ViewHolder for NormalViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        // Define elements of a row here
        SwipeLayout mSwipeView;
        BlurLayout mBlurLayout;
        Button mButton;
        ViewHolder(View itemView) {
            super(itemView);
            // Find view by ID and initialize here
        }

        void bindView(int position, ViewHolder vh) {
            // bindView() method to implement actions
        }
    }
}