package kevcon.ie.cloaked;

/**
 * Created by Intel Build on 07/03/2018.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

// a view pager adapter for the swipe tabs feature
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ContactsFragment tab1 = new ContactsFragment();
                return tab1;
            case 1:
                MessageFragment tab2 = new MessageFragment();
                return tab2;
            case 2:
                AboutFragment tab3 = new AboutFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}