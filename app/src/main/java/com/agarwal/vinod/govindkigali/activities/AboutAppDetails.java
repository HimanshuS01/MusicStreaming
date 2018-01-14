package com.agarwal.vinod.govindkigali.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.R;

public class AboutAppDetails extends AppCompatActivity {

    TextView privacy_policy, terms_conditions, about_company;
    boolean isPrivacyPolicyOpen = false;
    boolean isTermsConditionsOpen = false;
    boolean isAboutCompanyOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app_details);
        setTitle("About App");

        privacy_policy = findViewById(R.id.id_PrivacyPolicy);
        terms_conditions = findViewById(R.id.id_TermsnConditions);
        about_company = findViewById(R.id.id_AboutCompany);

    }

    public void PrivacyPolicyPressed(View view) {
        if (privacy_policy.getVisibility() == View.GONE) {
            privacy_policy.setVisibility(View.VISIBLE);
            isPrivacyPolicyOpen = true;
        } else {
            privacy_policy.setVisibility(View.GONE);
            isPrivacyPolicyOpen = false;
        }
    }

    public void TermsNConditionsPressed(View view) {
        if (terms_conditions.getVisibility() == View.GONE) {
            terms_conditions.setVisibility(View.VISIBLE);
            isTermsConditionsOpen = true;
        } else {
            terms_conditions.setVisibility(View.GONE);
            isTermsConditionsOpen = false;
        }
    }

    public void AboutCompanyPressed(View view) {
        if (about_company.getVisibility() == View.GONE) {
            about_company.setVisibility(View.VISIBLE);
            isAboutCompanyOpen = true;
        } else {
            about_company.setVisibility(View.GONE);
            isAboutCompanyOpen = false;
        }
    }
}
