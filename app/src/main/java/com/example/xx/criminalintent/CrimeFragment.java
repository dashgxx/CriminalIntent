package com.example.xx.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by XX on 2017/3/7.
 */

public class CrimeFragment extends Fragment {

    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mSuspectButton;
    private Button mReportButton;
    private Button mDialButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    private Crime mCrime;
    private File mPhotoFile;
    private boolean hasModified=false;

    private static final String ARG_CRIME_ID="crime_id";
    private static final String DIALOG_DATE="date";
    private static final String DIALOG_PHONE="phone";
    private static final String DIALOG_BIG_PICTURE="big_picture";
    private static final int REQUEST_DATE=0;
    private static final int REQUEST_CONTACT =1;
    private static final int REQUEST_PHONE=2;
    private static final int REQUEST_PHOTO=3;

    public static CrimeFragment newInstance(UUID id) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,id);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID uuid=(UUID) getArguments().getSerializable(ARG_CRIME_ID);

        mCrime=CrimeLab.get(getActivity()).getCrime(uuid);

        mPhotoFile=CrimeLab.get(getActivity()).getPhotoFile(mCrime);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_crime,container,false);

        final Intent takePhoto=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager pm=getActivity().getPackageManager();
        boolean availableCamera=takePhoto.resolveActivity(pm)!=null;

        mPhotoView=(ImageView)v.findViewById(R.id.crime_photo);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPhotoFile==null || !mPhotoFile.exists())
                    return;
                FragmentManager fm=getFragmentManager();
                BigPictureFrament bigPictureFrament=BigPictureFrament.newInstance(mPhotoFile.getPath());
                bigPictureFrament.show(fm,DIALOG_BIG_PICTURE);
            }
        });
        if(availableCamera){
            mPhotoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(mPhotoView.getWidth()==0)
                        return;
                    updatePhoto();
                    mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        //updatePhoto();

        mPhotoButton=(ImageButton)v.findViewById(R.id.crime_camera);
        mPhotoButton.setEnabled(availableCamera);
        if(availableCamera)
            takePhoto.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mPhotoFile));
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(takePhoto,REQUEST_PHOTO);
            }
        });

        mTitleField=(EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
                returnResult();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton=(Button) v.findViewById(R.id.crime_date);
        updateDate();
        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm=getFragmentManager();
                DialogFragment dialogFragment=DateickerFragment.newInstance(mCrime.getDate());
                dialogFragment.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialogFragment.show(fm,DIALOG_DATE);
            }
        });

        mSolvedCheckBox=(CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
                returnResult();
            }
        });

        final Intent pickContact=new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
        //pickContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton=(Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        mSuspectButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mCrime.getSuspect()==null)
                    return false;

                mCrime.setSuspect(null);
                mSuspectButton.setText(R.string.crime_suspect_button);
                return true;
            }
        });

        if (mCrime.getSuspect()!=null)
            mSuspectButton.setText(mCrime.getSuspect());
        if(pm.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY)==null)
            mSuspectButton.setEnabled(false);

        mDialButton=(Button)v.findViewById(R.id.crime_dial);
        mDialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mCrime.getSuspect()==null)
                {
                    Toast.makeText(getActivity(),R.string.toast_no_suspect, Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor cursor=getActivity().getContentResolver()
                        .query(Contacts.CONTENT_URI,
                                new String[]{Contacts._ID},
                                Contacts.DISPLAY_NAME+"= ?",
                                new String[]{mCrime.getSuspect()},
                                null);
                if(cursor==null)
                    return;
                long id;
                try{
                    if(cursor.getCount()==0)
                        return;

                    cursor.moveToFirst();
                    id=cursor.getLong(0);
                }finally {
                    cursor.close();
                }

                cursor=getActivity().getContentResolver()
                        .query(Phone.CONTENT_URI,
                                new String[]{Phone.NUMBER},
                                Phone.CONTACT_ID+" = ?",
                                new String[]{""+id},
                                null);
                if(cursor==null)
                    return;

                String[] buttonList;
                try{
                    if(cursor.getCount()==0)
                        return;

                    buttonList=new String[cursor.getCount()];
                    cursor.moveToFirst();
                    for(int i=0;i<cursor.getCount();i++)
                    {
                        buttonList[i]=cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                        cursor.moveToNext();
                    }
                }finally {
                    cursor.close();
                }

                PhonePickerFragment phonePickerFragment=PhonePickerFragment.newInstance(buttonList);
                phonePickerFragment.setTargetFragment(CrimeFragment.this,REQUEST_PHONE);
                FragmentManager fm=getFragmentManager();
                phonePickerFragment.show(fm,DIALOG_PHONE);
                //Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                //startActivity(intent);
            }
        });

        mReportButton=(Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
//                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
//                intent=Intent.createChooser(intent,getString(R.string.send_report_via));
                Intent intent= ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setChooserTitle(R.string.send_report_via)
                        .createChooserIntent();
                startActivity(intent);
            }
        });

        return v;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=Activity.RESULT_OK)
            return;
        if (requestCode==REQUEST_DATE) {
            if (data != null) {
                Date date = (Date) data.getSerializableExtra(DateickerFragment.ARG_DATE);
                mCrime.setDate(date);
                updateDate();
            }
        }
        else
            if(requestCode== REQUEST_CONTACT){
                if(data!=null)
                {
                    Uri uri=data.getData();
                    String[] queryFields=new String[]{Contacts.DISPLAY_NAME};

                    Cursor cursor=getActivity().getContentResolver().query(uri,queryFields,null,null,null);

                    if(cursor==null)
                        return;
                    try {
                        if(cursor.getCount()==0)
                            return;

                        cursor.moveToFirst();
                        String suspect=cursor.getString(0);
                        mCrime.setSuspect(suspect);
                        mSuspectButton.setText(suspect);
                    }finally {
                        cursor.close();
                    }
                }
        }
        else
                if (requestCode==REQUEST_PHONE){
                    if(data!=null)
                    {
                        String phoneNumber=(String) data.getSerializableExtra(PhonePickerFragment.EXTRA_PHONE_NUMBER);
                        Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phoneNumber));
                        startActivity(intent);
                    }
                }
        else
                    if(requestCode==REQUEST_PHOTO)
                        updatePhoto();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime.getId());
                getActivity().finish();
             default:
                 return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private void updateDate() {
        final String dateString = DateFormat.format("E,MMM dd,yyyy",mCrime.getDate()).toString();
        mDateButton.setText(dateString);
    }

    private void updatePhoto(){
        if( mPhotoFile==null || !mPhotoFile.exists())
            mPhotoView.setImageDrawable(null);
        else
        {
            Bitmap bitmap=
                    PictureUtils.getScaledBitmap(mPhotoFile.getPath(),
                            mPhotoView.getWidth(),mPhotoView.getHeight());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private String getCrimeReport()
    {
        String solvedString;
        if(mCrime.isSolved())
            solvedString=getString(R.string.crime_report_solved);
        else
            solvedString=getString(R.string.crime_report_unsolved);


        String dateString = DateFormat.format("EEE, MMM dd",mCrime.getDate()).toString();

        String suspect=mCrime.getSuspect();
        if(suspect!=null)
            suspect= getString(R.string.crime_report_suspect, suspect);
        else
            suspect=getString(R.string.crime_report_no_suspect);

        return getString(R.string.crime_report_template, mCrime.getTitle(),dateString,solvedString,suspect);
    }

    public void returnResult()
    {
        if(!hasModified)
        {
            Intent data=new Intent();
            int index=CrimeLab.get(getActivity()).getCrimes().indexOf(mCrime);
            data.putExtra(CrimeListFragment.EXTRA_CHANGED_INDEX,index);
            getActivity().setResult(Activity.RESULT_OK,data);
        }
    }
}
