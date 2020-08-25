package com.jesus.cproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.jesus.cproject.R
import com.jesus.cproject.adapters.RatesAdapter
import com.jesus.cproject.dialogues.RateDialog
import com.jesus.cproject.models.Rate
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_rates.view.*


class RatesFragment : Fragment() {

    private lateinit var _view: View

    private lateinit var adapter: RatesAdapter
    private val ratesList: ArrayList<Rate> = ArrayList()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var ratesDBref: CollectionReference

    private var ratesSubscription: ListenerRegistration? = null
    private lateinit var rateBusListener: Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_rates, container, false)


        setUpRatesDB()
        setUpCurrentUser()

        setUpFab()
        setUpRecyclerView()
        return _view
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = RatesAdapter(ratesList)
        _view.recyclerView.setHasFixedSize(true)
        _view.recyclerView.layoutManager = layoutManager
        _view.recyclerView.itemAnimator = DefaultItemAnimator()
        _view.recyclerView.adapter = adapter
    }
    private fun setUpFab() {
        _view.fabRating.setOnClickListener { RateDialog().show(fragmentManager!!, "") }

    }
    private fun setUpRatesDB(){
        ratesDBref = store.collection("rates")
    }
    private fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }
}