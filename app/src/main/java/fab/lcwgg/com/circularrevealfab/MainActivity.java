package fab.lcwgg.com.circularrevealfab;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Outline;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        private boolean mIsViewRevealed = false;
        private View mRevealView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final ImageButton addButton = (ImageButton) rootView.findViewById(R.id.fab_button);
            mRevealView = rootView.findViewById(R.id.view_reveal);

            setFabOutline(addButton);
            addButton.setOnClickListener(this);

            return rootView;
        }

        private void setFabOutline(final ImageButton fab) {

            fab.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int diameter = getResources().getDimensionPixelSize(R.dimen.round_button_diameter);
                    outline.setOval(0, 0, diameter, diameter);
                }
            });

            fab.setClipToOutline(true);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fab_button) {
                if (mIsViewRevealed) {
                    mIsViewRevealed = false;
                    setCircularHideEffect((ImageButton)v);
                } else {
                    mIsViewRevealed = true;
                    setCircularRevealEffect((ImageButton)v);
                }
            }
        }

        private void setCircularRevealEffect(final ImageButton fab) {
            // get the center for the clipping circle
            // We add the margin to the fab position
            final int cx = (fab.getLeft() + (getResources().getDimensionPixelSize(R.dimen.fab_margin) * 2));
            final int cy = (fab.getTop() + (getResources().getDimensionPixelSize(R.dimen.fab_margin) * 2));

            // get the initial radius for the clipping circle
            final int finalRadius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

            // create the animator for this view (the start radius is zero)
            final Animator anim =
                    ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, finalRadius);

            anim.setDuration(500);

            // make the view visible and start the animation
            mRevealView.setVisibility(View.VISIBLE);
            anim.start();
        }

        private void setCircularHideEffect(final ImageButton fab) {
            // get the center for the clipping circle
            // We add the margin to the fab position
            final int cx = (fab.getLeft() + (getResources().getDimensionPixelSize(R.dimen.fab_margin) * 2));
            final int cy = (fab.getTop() + (getResources().getDimensionPixelSize(R.dimen.fab_margin) * 2));

            // get the initial radius for the clipping circle
            int initialRadius = mRevealView.getWidth();

            // create the animation (the final radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, initialRadius, 0);
            anim.setDuration(500);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    mRevealView.setVisibility(View.INVISIBLE);
                }
            });

            // start the animation
            anim.start();
        }
    }
}
