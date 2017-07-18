package ru.tohaman.rubicsguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import ru.tohaman.rubicsguide.about.AboutActivity;
import ru.tohaman.rubicsguide.about.AboutFragment;
import ru.tohaman.rubicsguide.blind.BlindMenuActivity;
import ru.tohaman.rubicsguide.g2f.G2FActivity;
import ru.tohaman.rubicsguide.listpager.ListActivity;
import ru.tohaman.rubicsguide.listpager.ListFragment;
import ru.tohaman.rubicsguide.listpager.ListPager;

public class MainActivity extends SingleFragmentActivity implements MainFragment.Callbacks, ListFragment.Callbacks {
    private Intent mIntent;
    private String phase;

    @Override
    protected Fragment createFragment () {
        return new MainFragment();
    }

    @Override
    protected int getLayoutResId () {
        return R.layout.activity_maindetail;
    }

    @Override
    // действие для фрагмента ListFragment
    public void onItemSelected(ListPager listPager) {
        Toast.makeText(this,listPager.getDescription(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            phase = savedInstanceState.getString("phase");
        }
    }

    @Override
    // действие для фрагмента PagerFragment
    public void onMainItemSelected (int id) {
        phase = getResources().getStringArray(R.array.main_phase)[id];
        switch (phase) {
            case "BEGIN":
                mIntent = ListActivity.newIntenet(this, phase);
                startActivity(mIntent);
                break;

            case "G2F":
                mIntent = new Intent(this, G2FActivity.class);
                startActivity(mIntent);
                break;

            case "BLIND":
                mIntent = new Intent(this,BlindMenuActivity.class);
                startActivity(mIntent);
                break;

            case "BASIC":
                if (findViewById(R.id.detail_fragment_container) == null) {
                    // для телефона вызываем новую активность
                    mIntent = ListActivity.newIntenet(this, phase);
                    startActivity(mIntent);
                } else {
                    // для планшета открываем во фрагменте
                    Fragment newDetail = ListFragment.newInstance(phase);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_fragment_container, newDetail)
                            .commit();
                }
                break;

            case "ABOUT":
                if (findViewById(R.id.detail_fragment_container) == null) {
                    // для телефона вызываем новую активность
                    mIntent = new Intent(this, AboutActivity.class);
                    startActivity(mIntent);
                    break;
                } else {
                    // для планшета открываем в правом фрагменте
                    Fragment newDetail = new AboutFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_fragment_container, newDetail)
                            .commit();
                }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("phase",phase);
    }

    @Override
    protected void onStart () {
        super.onStart();

        if (findViewById(R.id.detail_fragment_container) != null) {
            if (phase.equals("BASIC")) {
                Fragment newDetail = new AboutFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetail)
                        .commit();
            } else { // Если не Basic, значит открываем окно About
                Fragment newDetail = ListFragment.newInstance(phase);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetail)
                        .commit();
            }
        }
    }

}
