package com.codebosses.flicks.adapters.exapndablerecyclerviewadapter;

import android.content.Context;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.codebosses.flicks.pojo.expandrecyclerviewpojo.CategoryHeader;
import com.codebosses.flicks.R;
import com.codebosses.flicks.utils.FontUtils;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class CategoryHeaderHolder extends GroupViewHolder {

    public TextView genreName;
    public ImageView arrow;
    private ImageView icon;
    public View viewDivider;

    public CategoryHeaderHolder(View itemView, Context context) {
        super(itemView);
        genreName = (TextView) itemView.findViewById(R.id.list_item_genre_name);
        arrow = (ImageView) itemView.findViewById(R.id.list_item_genre_arrow);
        icon = (ImageView) itemView.findViewById(R.id.list_item_genre_icon);
        viewDivider = itemView.findViewById(R.id.dividerItemGenreHeaderRow);

        FontUtils.getFontUtils(context).setTextViewRegularFont(genreName);
    }

    public void setGenreTitle(ExpandableGroup genre) {
        if (genre instanceof CategoryHeader) {
            genreName.setText(genre.getTitle());
            icon.setImageResource(((CategoryHeader) genre).getIconResId());
        }
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}
