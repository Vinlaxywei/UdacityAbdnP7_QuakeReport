package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public static class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_min_magnitude_key)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.setting_order_by_key)));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // 设置 preference summary
            if (preference instanceof ListPreference) {
                ListPreference listpref = (ListPreference) preference;
                int prefIndex = listpref.findIndexOfValue(newValue.toString());
                listpref.setSummary(listpref.getEntries()[prefIndex]);
            } else {
                preference.setSummary(newValue.toString());
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            // 对偏好设定设置监听器
            preference.setOnPreferenceChangeListener(this);
            // 获取当前的值
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            // 更新显示
            onPreferenceChange(preference, preferenceString);
        }
    }

}
