package com.isuhuo.newflash.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017-08-25.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private final Context context;

    private List<Fragment> fragments;

    private final CharSequence[] titles;

    private final FragmentManager fm;

    public FragmentAdapter(FragmentManager fm, Context context,
                           List<Fragment> fragments, CharSequence[] titles) {
        super(fm);
        this.fm = fm;
        this.context = context;
        this.fragments = fragments;
        this.titles = titles;
    }

    public FragmentAdapter(FragmentManager fm, Context context,
                           List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.context = context;
        this.fragments = fragments;
        this.titles = null;
    }

	/*@Override
	public int getItemPosition(Object object) {
		for (int i = 0; i < this.fragments.size(); i++) {
			if (object.equals(this.fragments.get(i)))
				return i;
		}
		return POSITION_NONE;
	}*/

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @SuppressWarnings("unchecked")
    public <T extends Fragment> T getItemFragment(int position) {
        return (T) fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? super.getPageTitle(position) : titles[position];
    }

}
