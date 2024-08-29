package com.iambhargavnath.arduinoremote;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pdfview.PDFView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ListView bluetoothDeviceList;
    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter btAdapter;

    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;

    ArrayList<String> bluetoothList, bluetoothMAC;

    private ImageButton bluetoothBtn;
    private ImageButton upBtn, leftBtn, rightBtn, downBtn;
    private ImageButton leftIndicator, rightIndicator;
    private Button reverseBtn;
    private ImageButton uTurn;
    private ImageButton warningBtn;
    private Button helpBtn, infoBtn;
    private ToggleButton aBtn, bBtn, cBtn, dBtn;

    private int leftIndicatorFlag = 0;
    private int rightIndicatorFlag = 0;
    private int reverseFlag = 0;
    private int warningFlag = 0;

    String command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothBtn = (ImageButton) findViewById(R.id.bluetoothBtn);
        helpBtn = (Button) findViewById(R.id.helpBtn);
        infoBtn = (Button) findViewById(R.id.infoBtn);

        aBtn = (ToggleButton) findViewById(R.id.aBtn);
        bBtn = (ToggleButton) findViewById(R.id.bBtn);
        cBtn = (ToggleButton) findViewById(R.id.cBtn);
        dBtn = (ToggleButton) findViewById(R.id.dBtn);

        leftIndicator = (ImageButton) findViewById(R.id.leftIndicator);
        rightIndicator = (ImageButton) findViewById(R.id.rightIndicator);

        upBtn = (ImageButton) findViewById(R.id.upBtn);
        downBtn = (ImageButton) findViewById(R.id.downBtn);
        leftBtn = (ImageButton) findViewById(R.id.leftBtn);
        rightBtn = (ImageButton) findViewById(R.id.rightBtn);

        reverseBtn = (Button) findViewById(R.id.reverseBtn);
        uTurn = (ImageButton) findViewById(R.id.uTurn);

        warningBtn = (ImageButton) findViewById(R.id.warningBtn);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        bluetoothList = new ArrayList<>();
        bluetoothMAC = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkBluetoothState();
        }

        bluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btAdapter.isEnabled()) {
                    showDevicesDialog();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        checkBluetoothState();
                    }
                }
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserManual("How to Use?");
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserManual("About Us");
            }
        });

        leftIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(leftIndicatorFlag==0)
                {
                    command = "5";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    leftIndicator.setBackground(getDrawable(R.drawable.circle_blue));
                    leftIndicator.setColorFilter(Color.WHITE);
                    leftIndicatorFlag = 1;
                }
                else
                {
                    command = "6";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    leftIndicator.setBackground(getDrawable(R.drawable.bluecircle));
                    leftIndicator.setColorFilter(Color.BLUE);
                    leftIndicatorFlag = 0;
                }
            }
        });

        rightIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rightIndicatorFlag==0)
                {
                    command = "7";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    rightIndicator.setBackground(getDrawable(R.drawable.circle_blue));
                    rightIndicator.setColorFilter(Color.WHITE);
                    rightIndicatorFlag = 1;
                }
                else
                {
                    command = "8";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    rightIndicator.setBackground(getDrawable(R.drawable.bluecircle));
                    rightIndicator.setColorFilter(Color.BLUE);
                    rightIndicatorFlag = 0;
                }
            }
        });

        uTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uTurn.setBackground(getDrawable(R.drawable.circle_blue));
                uTurn.setColorFilter(Color.WHITE);
                command = "9";
                try {
                    outputStream.write(command.getBytes());
                } catch (NullPointerException e){e.printStackTrace();}
                catch (IOException e) {
                    e.printStackTrace();
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        uTurn.setBackground(getDrawable(R.drawable.bluecircle));
                        uTurn.setColorFilter(Color.BLUE);
                    }
                }, 60);
            }
        });

        reverseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reverseFlag==0)
                {
                    reverseFlag = 1;
                    reverseBtn.setBackground(getDrawable(R.drawable.circle_blue));
                    reverseBtn.setTextColor(Color.WHITE);
                }
                else
                {
                    reverseFlag = 0;
                    reverseBtn.setBackground(getDrawable(R.drawable.bluecircle));
                    reverseBtn.setTextColor(Color.BLUE);
                }
            }
        });

        warningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(warningFlag==0)
                {
                    command = "W";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    warningBtn.setBackground(getDrawable(R.drawable.redbtn_dark));
                    warningBtn.setColorFilter(Color.WHITE);
                    warningFlag = 1;
                }
                else
                {
                    command = "X";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    warningBtn.setBackground(getDrawable(R.drawable.warning_btn));
                    warningBtn.setColorFilter(Color.BLACK);
                    warningFlag = 0;
                }
            }
        });

        upBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "1";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    upBtn.setBackground(getDrawable(R.drawable.circle_dark));
                    upBtn.setColorFilter(Color.WHITE);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    command = "0";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    upBtn.setBackground(getDrawable(R.drawable.circle));
                    upBtn.setColorFilter(Color.BLACK);
                }
                return false;
            }
        });

        leftBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(reverseFlag==0) {
                        command = "2";
                        try {
                            outputStream.write(command.getBytes());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        command = "L";
                        try {
                            outputStream.write(command.getBytes());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    leftBtn.setBackground(getDrawable(R.drawable.circle_dark));
                    leftBtn.setColorFilter(Color.WHITE);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    command = "0";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    leftBtn.setBackground(getDrawable(R.drawable.circle));
                    leftBtn.setColorFilter(Color.BLACK);
                }
                return false;
            }
        });

        rightBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(reverseFlag==0) {
                        command = "3";
                        try {
                            outputStream.write(command.getBytes());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        command = "R";
                        try {
                            outputStream.write(command.getBytes());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    rightBtn.setBackground(getDrawable(R.drawable.circle_dark));
                    rightBtn.setColorFilter(Color.WHITE);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    command = "0";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    rightBtn.setBackground(getDrawable(R.drawable.circle));
                    rightBtn.setColorFilter(Color.BLACK);
                }
                return false;
            }
        });

        downBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "4";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    downBtn.setBackground(getDrawable(R.drawable.circle_dark));
                    downBtn.setColorFilter(Color.WHITE);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    command = "0";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    downBtn.setBackground(getDrawable(R.drawable.circle));
                    downBtn.setColorFilter(Color.BLACK);
                }
                return false;
            }
        });

        aBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    command = "A";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    aBtn.setBackground(getDrawable(R.drawable.darkbtn));
                    aBtn.setTextColor(Color.WHITE);
                } else {
                    command = "E";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    aBtn.setBackground(getDrawable(R.drawable.lightbtn));
                    aBtn.setTextColor(Color.parseColor("#0026FF"));
                }
            }
        });

        bBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    command = "B";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    bBtn.setBackground(getDrawable(R.drawable.darkbtn));
                    bBtn.setTextColor(Color.WHITE);
                } else {
                    command = "F";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    bBtn.setBackground(getDrawable(R.drawable.lightbtn));
                    bBtn.setTextColor(Color.parseColor("#0026FF"));
                }
            }
        });

        cBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    command = "C";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    cBtn.setBackground(getDrawable(R.drawable.darkbtn));
                    cBtn.setTextColor(Color.WHITE);
                } else {
                    command = "G";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    cBtn.setBackground(getDrawable(R.drawable.lightbtn));
                    cBtn.setTextColor(Color.parseColor("#0026FF"));
                }
            }
        });

        dBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    command = "D";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    dBtn.setBackground(getDrawable(R.drawable.darkbtn));
                    dBtn.setTextColor(Color.WHITE);
                } else {
                    command = "H";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (NullPointerException e){e.printStackTrace();}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    dBtn.setBackground(getDrawable(R.drawable.lightbtn));
                    dBtn.setTextColor(Color.parseColor("#0026FF"));
                }
            }
        });


    }

    public void showDevicesDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.bluetooth_devices, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        bluetoothDeviceList = (ListView) dialogView.findViewById(R.id.bluetoothDeviceList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, bluetoothList);
        bluetoothDeviceList.setAdapter(adapter);

        bluetoothDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(BTinit(bluetoothMAC.get(position).trim()))
                {
                    if(BTconnect())
                    {
                        alertDialog.dismiss();
                    }
                }
            }
        });


    }

    public void showUserManual(String string) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.pdfdialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        LinearLayout okBtn = (LinearLayout) dialogView.findViewById(R.id.okBtn);
        TextView titleText = (TextView) dialogView.findViewById(R.id.titleText);
        PDFView pdfView = (PDFView) dialogView.findViewById(R.id.pdfView);

        if(string.trim().equals("How to Use?"))
        {
            titleText.setText(string);
            pdfView.fromAsset("how_to_use.pdf").show();
        }
        else if(string.trim().equals("About Us"))
        {
            titleText.setText(string);
            pdfView.fromAsset("about_us.pdf").show();
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if(resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    checkBluetoothState();
                }
            }
            else
            {
                showToast("Request Denied");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkBluetoothState() {
        Dexter.withActivity(this)
                // below line is use to request the number of permissions which are required in our app.
                .withPermissions(
                        // below is the list of permissions
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH_CONNECT)
                // after adding permissions we are calling an with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            // do you work now
                            // Checks for the Bluetooth support and then makes sure it is turned on
                            // If it isn't turned on, request to turn it on
                            // List paired devices
                            if(btAdapter==null) {
                                showToast("Bluetooth NOT supported. Aborting.");
                                return;
                            } else {
                                if (btAdapter.isEnabled()) {
                                    // Listing paired devices
                                    bluetoothList = new ArrayList<>();
                                    bluetoothMAC = new ArrayList<>();
                                    Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
                                    for (BluetoothDevice device : devices) {
                                        bluetoothList.add(device.getName());
                                        bluetoothMAC.add(device.getAddress());
                                    }
                                } else {
                                    //Prompt user to turn on Bluetooth
                                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                                }
                            }
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(error -> {
                })
                .onSameThread().check();
    }

    //Initializes bluetooth module
    @SuppressLint("MissingPermission")
    public boolean BTinit(String DEVICE_ADDRESS)
    {
        boolean found = false;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);

            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    @SuppressLint("MissingPermission")
    public boolean BTconnect()
    {
        boolean connected = true;

        try
        {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();

            Toast.makeText(getApplicationContext(),
                    "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            showToast("Bluetooth Connection Failed!");
            connected = false;
        }

        if(connected)
        {
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return connected;
    }

    private void showToast(String s)
    {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }
}