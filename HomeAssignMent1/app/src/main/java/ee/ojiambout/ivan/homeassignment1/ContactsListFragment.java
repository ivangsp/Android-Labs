package ee.ojiambout.ivan.homeassignment1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by ivan on 10/1/2017.
 */

public class ContactsListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener, SearchView.OnCloseListener  {

    private SimpleCursorAdapter mAdapter;
    private String mSearchString = null;
    private SearchView mSearchView;

    // Defining a projection
    @SuppressLint("InlinedApi")
    private static String DISPLAY_NAME_COMPAT = Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
            ContactsContract.Contacts.DISPLAY_NAME;


    private static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
            ContactsContract.Contacts._ID,
            DISPLAY_NAME_COMPAT,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts.LOOKUP_KEY
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        getLoaderManager().initLoader(0, null, this);

        mAdapter = new IndexedListAdapter(
                this.getActivity(),
                R.layout.list_item,
                null,
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
                new int[] {R.id.display_name});

        setListAdapter(mAdapter);
        getListView().setFastScrollEnabled(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ViewHolder viewHolder = (ViewHolder) v.getTag();
        String name = viewHolder.contactName.getText().toString();
        String phoneNumber = viewHolder.phoneNumber.getText().toString();
        String email = viewHolder.emailAddress.getText().toString();

        Intent intent= new Intent(getContext(),ContactsDetailsActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("email",email);
        intent.putExtra("phoneNumber",phoneNumber);
        getContext().startActivity(intent);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        Uri baseUri;

        if (mSearchString != null) {
            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(mSearchString));
        } else {
            baseUri = ContactsContract.Contacts.CONTENT_URI;
        }

        String selection = "((" + DISPLAY_NAME_COMPAT + " NOTNULL) AND ("
                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + DISPLAY_NAME_COMPAT + " != '' ))";

        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        // Starts the query
        return new CursorLoader(
                getActivity(),
                baseUri,
                CONTACTS_SUMMARY_PROJECTION,
                selection,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Put the result Cursor in the adapter for the ListView
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Deelte the reference to the existing Cursor
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean onClose() {
        if (!TextUtils.isEmpty(mSearchView.getQuery())) {
            mSearchView.setQuery(null, true);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String newFilter = !TextUtils.isEmpty(newText) ? newText : null;

        if (mSearchString == null && newFilter == null) {
            return true;
        }
        if (mSearchString != null && mSearchString.equals(newFilter)) {
            return true;
        }
        mSearchString = newFilter;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    static class ViewHolder{
        TextView contactName;
        TextView phoneLabel;
        TextView phoneNumber;
        TextView emailLabel;
        TextView emailAddress;
        View separator;
        View separator_2;
        ContactDetailsLookupTask contactDetailsLookupTask;
    }

    class IndexedListAdapter extends SimpleCursorAdapter implements SectionIndexer {

        AlphabetIndexer alphaIndexer;

        public IndexedListAdapter(Context context, int layout, Cursor c,
                                  String[] from, int[] to) {
            super(context, layout, c, from, to, 0);
        }

        @Override
        public Cursor swapCursor(Cursor c) {
            if (c != null) {
                alphaIndexer = new AlphabetIndexer(c,
                        c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME),
                        " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            }

            return super.swapCursor(c);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                LayoutInflater inflater = getLayoutInflater(null);
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.contactName = (TextView) convertView.findViewById(R.id.display_name);
                viewHolder.phoneLabel = (TextView) convertView.findViewById(R.id.phone_label);
                viewHolder.phoneNumber = (TextView) convertView.findViewById(R.id.phone_number);
                viewHolder.separator = convertView.findViewById(R.id.label_separator);
                viewHolder.emailLabel = (TextView) convertView.findViewById(R.id.email_label);
                viewHolder.emailAddress = (TextView) convertView.findViewById(R.id.email_address);
                viewHolder.separator_2 = convertView.findViewById(R.id.elabel_separator);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                viewHolder.contactDetailsLookupTask.cancel(true);
            }

            return super.getView(position, convertView, parent);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);

            long contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.contactDetailsLookupTask = new ContactDetailsLookupTask(view);
            viewHolder.contactDetailsLookupTask.execute(contactId);
        }

        @Override
        public int getPositionForSection(int section) {
            return alphaIndexer.getPositionForSection(section);
        }

        @Override
        public int getSectionForPosition(int position) {
            return alphaIndexer.getSectionForPosition(position);
        }

        @Override
        public Object[] getSections() {
            return alphaIndexer == null ? null : alphaIndexer.getSections();
        }
    }


    private class ContactDetailsLookupTask extends AsyncTask<Long, Void, Void> {
        final WeakReference<View> mViewReference;

        String mPhoneNumber;
        String mPhoneLabel;
        String mEmailAddress;
        String mEmailLabel;

        public ContactDetailsLookupTask(View view){
            mViewReference = new WeakReference<>(view);
        }

        @Override
        protected Void doInBackground(Long... ids) {
            String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.LABEL, ContactsContract.CommonDataKinds.Email.TYPE,
                    ContactsContract.CommonDataKinds.Email.DATA, ContactsContract.CommonDataKinds.Email.LABEL};
            String[] email_projection = new String[] {ContactsContract.CommonDataKinds.Email.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.ADDRESS,
                    ContactsContract.CommonDataKinds.Email.LABEL};
            long contactId = ids[0];

            final Cursor phoneCursor = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    ContactsContract.Data.CONTACT_ID + "=?",
                    new String[]{String.valueOf(contactId)},
                    null);

            final Cursor emailCursor = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    email_projection,
                    ContactsContract.Data.CONTACT_ID + "=?",
                    new String[]{String.valueOf(contactId)},
                    null);

            if(phoneCursor != null && phoneCursor.moveToFirst() && phoneCursor.getCount() == 1) {
                final int contactNumberColumnIndex 	= phoneCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER);

                mPhoneNumber = phoneCursor.getString(contactNumberColumnIndex);
                int type = phoneCursor.getInt(phoneCursor.getColumnIndexOrThrow(
                        ContactsContract.CommonDataKinds.Phone.TYPE));
                mPhoneLabel = phoneCursor.getString(phoneCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.LABEL));
                mPhoneLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(
                        getResources(), type, mPhoneLabel).toString();

                phoneCursor.close();
            }

            if(emailCursor != null && emailCursor.moveToFirst() && emailCursor.getCount() == 1) {
                final int contactEmailColumnIndex 	= emailCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Email.ADDRESS);

                mEmailAddress = emailCursor.getString(emailCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Email.ADDRESS));
                int etype = emailCursor.getInt(emailCursor.getColumnIndexOrThrow(
                            ContactsContract.CommonDataKinds.Email.TYPE));
                mEmailLabel = emailCursor.getString(emailCursor.getColumnIndex(
                              ContactsContract.CommonDataKinds.Email.LABEL));
                mEmailLabel = ContactsContract.CommonDataKinds.Email.getTypeLabel(getResources(),
                              etype, mEmailLabel).toString();

                emailCursor.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            View view = mViewReference.get();
            if (view != null ){
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                if (mPhoneNumber != null){
                    viewHolder.phoneNumber.setText(mPhoneNumber);
                    viewHolder.phoneLabel.setText(mPhoneLabel);
                    viewHolder.phoneLabel.setVisibility(View.VISIBLE);
                    viewHolder.separator.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.phoneNumber.setText(getString(R.string.label_multiple_nos));
                    viewHolder.phoneLabel.setVisibility(View.GONE);
                    viewHolder.separator.setVisibility(View.GONE);
                }

                if (mEmailAddress != null){
                    viewHolder.emailAddress.setText(mEmailAddress);
                    viewHolder.emailLabel.setText(mEmailLabel);
                    viewHolder.emailLabel.setVisibility(View.VISIBLE);
                    viewHolder.separator_2.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.emailAddress.setText(getString(R.string.label_multiple_addresses));
                    viewHolder.emailLabel.setVisibility(View.GONE);
                    viewHolder.separator_2.setVisibility(View.GONE);
                }

            }
        }
    }


}