package ee.ojiambout.ivan.homeassignment1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ivan on 10/2/2017.
 */


/**
 * Created by gsp on 12/06/16.
 */
public  class CustomAdaptor extends ArrayAdapter<Contact>{
    private  List<Contact> contactDetailses;
    private  final Context context;

    public CustomAdaptor(Context context, List<Contact> contactDetailses){
        super(context, R.layout.list_item, contactDetailses);
        this.context = context;
        this.contactDetailses = contactDetailses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater minflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Contact contactdetails = getItem(position);
        View row    = minflater.inflate(R.layout.list_item, parent, false);
        TextView nametextview = (TextView) row.findViewById(R.id.name);
        nametextview.setText(contactdetails.getName());

        return  row;

    }


}



//public class CustomAdaptor extends BaseAdapter {
//
//    Context context;
//    List<Contact>  contacts;
//
//    public  CustomAdaptor(Context context, List<Contact> contacts){
//
//        this.context=context;
//        contacts = contacts;
//    }
//
//    private class ViewHolder {
//        TextView name;
//
//    }
//    @Override
//    public int getCount() {
//        return contacts.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return contacts.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return contacts.indexOf(getItem(position));
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder holder=null;
//        LayoutInflater minflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View row=convertView;
//        if(row==null){
//            row= minflater.inflate(R.layout.list_item, parent,false);
//            holder=new ViewHolder();
//            holder.name=(TextView)row.findViewById(R.id.name);
//            row.setTag(holder);
//        }
//        else{
//
//            holder=(ViewHolder)row.getTag();
//
//        }
//        Contact contactDetails= (Contact) getItem(position);
//        holder.name.setText(contactDetails.getName());
//
//
//        return row;
//    }
//
//
//}
//
