package com.example.crud.home;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.example.crud.R;
import com.example.crud.fragment.FirstFragment;
import com.example.crud.fragment.SecondFragment;
import com.example.crud.models.Cliente;
import com.example.crud.models.ClienteDAO;
import com.example.crud.recyclerView.ClienteAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crud.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import static com.google.android.material.internal.ContextUtils.getActivity;

public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    Intent intent;
    private List<Cliente> clientes = null;
    RecyclerView recyclerView;
    Cliente clienteEditado;
    ClienteAdapter adapter;
    ClienteDAO dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();
        dao = new ClienteDAO(getBaseContext());
        clientes = dao.retornarTodos();
        adapter = new ClienteAdapter(clientes);

        setSupportActionBar(binding.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.includemain).setVisibility(View.INVISIBLE);
                findViewById(R.id.includecadastro).setVisibility(View.VISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });

        Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.includemain).setVisibility(View.VISIBLE);
                findViewById(R.id.includecadastro).setVisibility(View.INVISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);

            }
        });

        Button btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtNome = (EditText) findViewById(R.id.txtNome);
                Spinner spnEstado = (Spinner) findViewById(R.id.spnEstado);
                RadioGroup rgSexo = (RadioGroup) findViewById(R.id.rgSexo);
                CheckBox chkVip = (CheckBox) findViewById(R.id.chkVip);

                String nome = txtNome.getText().toString();
                String uf = spnEstado.getSelectedItem().toString();
                boolean vip = chkVip.isChecked();
                String sexo = rgSexo.getCheckedRadioButtonId() == R.id.rbMasculino ? "M" : "F";




                boolean sucesso;
                if (clienteEditado != null)
                    sucesso = dao.salvar(clienteEditado.getId(), nome, sexo, uf, vip);
                else
                    sucesso = dao.salvar(nome, sexo, uf,vip);

                if(sucesso){
                    Cliente cliente= dao.retornarUltimo();

                    if(clienteEditado != null){
                        adapter.atualizarCliente(cliente);
                        clienteEditado = null;
                    } else if (adapter != null) {

                        adapter.adicionarCliente(cliente);
                    } else {
                        clientes = dao.retornarTodos();
                        clientes.add(cliente);
                        adapter.adicionarCliente(cliente);
                        configurarRecycler();
                    }
                }
            }
        });


        if (intent.hasExtra("cliente")){
            findViewById(R.id.includemain).setVisibility(View.INVISIBLE);
            findViewById(R.id.includecadastro).setVisibility(View.VISIBLE);
            findViewById(R.id.fab).setVisibility(View.INVISIBLE);
            clienteEditado = (Cliente) intent.getSerializableExtra("cliente");
            EditText txtNome = (EditText)findViewById(R.id.txtNome);
            Spinner spnEstado = (Spinner)findViewById(R.id.spnEstado);
            CheckBox chkVip = (CheckBox)findViewById(R.id.chkVip);

            txtNome.setText(clienteEditado.getNome());
            chkVip.setChecked(clienteEditado.getVip());
            spnEstado.setSelection(getIndex(spnEstado,clienteEditado.getUf()));
            if(clienteEditado.getSexo() !=  null) {

                RadioButton rb;
                if (clienteEditado.getSexo().equals("M"))
                     rb = (RadioButton) findViewById(R.id.rbMasculino);
                 else
                     rb = (RadioButton) findViewById(R.id.rbFeminino);
                 rb.setChecked(true);
            }
        }
    }


    private void configurarRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ClienteDAO dao = new ClienteDAO(this);
        clientes = dao.retornarTodos();
        adapter = new ClienteAdapter(clientes);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    private  int getIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i = 0; i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }


}



