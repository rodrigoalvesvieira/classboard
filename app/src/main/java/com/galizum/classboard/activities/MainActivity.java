package com.galizum.classboard.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.galizum.classboard.MainApplication;
import com.galizum.classboard.R;
import com.galizum.classboard.database.ClassDbHelper;
import com.galizum.classboard.util.Logger;
import com.r0adkll.postoffice.PostOffice;
import com.r0adkll.postoffice.model.Design;
import com.r0adkll.postoffice.styles.EditTextStyle;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    TextView textView;

    static String cameraTabTitle;
    static String classesTabTitle;

    static private SecureRandom random = new SecureRandom();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        cameraTabTitle = getResources().getString(R.string.camera_tab_title);
        classesTabTitle = getResources().getString(R.string.classes_tab_title);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new LaunchpadSectionFragment();

                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new ClassesSectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(ClassesSectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return cameraTabTitle;

            return classesTabTitle;
        }
    }

    public void setFonts() {
        Typeface fontOthers = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
        textView.setTypeface(fontOthers);

        try {
            final int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
            TextView title = (TextView) getWindow().findViewById(titleId);
            Typeface fontLogo = Typeface.createFromAsset(getAssets(), "HouseSlant-Regular.otf");

            title.setTypeface(fontLogo);
            // check for null and manipulate the title as see fit
        } catch (Exception e) {
            Logger.e(MainApplication.TAG, "Failed to obtain action bar title reference");
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class LaunchpadSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_camera, container, false);

            // Demonstration of a collection-browsing activity.
            rootView.findViewById(R.id.demo_collection_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), NavigationActivity.class);
                            startActivity(intent);
                        }
                    });

            // Demonstration of navigating to external activities.
            rootView.findViewById(R.id.select_pictures_from_gallery)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create an intent that asks the user to pick a photo, but using
                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
                            // the application from the device home screen does not return
                            // to the external activity.
                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
                            externalActivityIntent.setType("image/*");
                            externalActivityIntent.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            startActivity(externalActivityIntent);
                        }
                    });

            return rootView;
        }
    }

    /**
     * A static method to create a new class object from the parameters it receives
     *
     * @param ctx, the current Activity
     * @param text the title of the class to be created
     */
    public static void saveClassOnDatabase(Context ctx, String text) {
        ClassDbHelper classDbHelper = new ClassDbHelper(ctx);
        SQLiteDatabase db = classDbHelper.getWritableDatabase();
        classDbHelper.onCreate(db);

        // Writing values to DB
        ContentValues values = new ContentValues();

        String randomId = new BigInteger(80, random).toString(32);
        Logger.d(MainApplication.TAG, "Salvando nova disciplina com id " + randomId +  "...");

        values.put("classId", randomId);
        values.put("title", text);
        db.insert(ClassDbHelper.TABLE_NAME, "null", values);

        Toast.makeText(ctx, ctx.getResources().getString(R.string.class_saved), Toast.LENGTH_SHORT).show();
    }

    /**
     * A fragment for the Classes menu, with a ListView and a button to add another class
     */
    public static class ClassesSectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_classes, container, false);
            Bundle args = getArguments();

            ListView classesList = (ListView) rootView.findViewById(R.id.list);

            rootView.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    PostOffice.newMail(getActivity())
                            .setTitle(getResources().getString(R.string.class_title))
                            .setIcon(R.drawable.ic_launcher)
                            .setThemeColor(getResources().getColor(R.color.dark_green))
                            .setDesign(Design.MATERIAL_LIGHT)
                            .showKeyboardOnDisplay(true)
                            .setButton(Dialog.BUTTON_POSITIVE, R.string.save, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setButton(Dialog.BUTTON_NEGATIVE, R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setStyle(new EditTextStyle.Builder(getActivity())
                                    .setHint(getResources().getString(R.string.insert_class))
                                    .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                                    .setOnTextAcceptedListener(new EditTextStyle.OnTextAcceptedListener() {

                                        @Override
                                        public void onAccepted(String classTitle) {
                                            if (classTitle.length() > 0) {
                                                saveClassOnDatabase(getActivity(), classTitle);
                                            } else {
                                                new AlertDialog.Builder(getActivity())
                                                        .setTitle(getResources().getString(R.string.error))
                                                        .setMessage(getResources().getString(R.string.class_not_saved))
                                                        .setNeutralButton(getResources().getString(R.string.close), null)
                                                        .show();
                                            }
                                        }
                                    }).build())
                            .show(getFragmentManager());
                }
            });

            return rootView;
        }
    }
}
