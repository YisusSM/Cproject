package com.jesus.cproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.jesus.cproject.R
import com.jesus.cproject.adapters.RatesAdapter
import com.jesus.cproject.dialogues.RateDialog
import com.jesus.cproject.models.NewRateEvent
import com.jesus.cproject.models.Rate
import com.jesus.cproject.toast
import com.jesus.cproject.utils.RxBus
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_rates.view.*
import java.util.EventListener


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

    private lateinit var scrollListener: RecyclerView.OnScrollListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_rates, container, false)


        setUpRatesDB()
        setUpCurrentUser()

        setUpFab()
        setUpRecyclerView()

        subscribeToNewRatings()
        subscribeToRatings()
        return _view
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = RatesAdapter(ratesList)
        _view.recyclerView.setHasFixedSize(true)
        _view.recyclerView.layoutManager = layoutManager
        _view.recyclerView.itemAnimator = DefaultItemAnimator()
        _view.recyclerView.adapter = adapter

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    _view.fabRating.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && _view.fabRating.isShown) {
                    _view.fabRating.hide()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        }
        _view.recyclerView.addOnScrollListener(scrollListener)
    }

    private fun setUpFab() {
        _view.fabRating.setOnClickListener { RateDialog().show(fragmentManager!!, "") }

    }

    private fun setUpRatesDB() {
        ratesDBref = store.collection("rates")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun saveRate(rate: Rate) {
        val newRating = HashMap<String, Any>()
        newRating["text"] = rate.text
        newRating["rate"] = rate.rate
        newRating["createdAt"] = rate.createAt
        newRating["profileImgUrl"] = rate.profileImgUrl
        newRating["userId"] = rate.userId

        ratesDBref.add(newRating).addOnCompleteListener {
            activity!!.toast("rating added!!")
        }.addOnFailureListener {
            activity!!.toast("Rating error, try again!!!")
        }
    }

    private fun subscribeToNewRatings() {
        rateBusListener = RxBus.listen(NewRateEvent::class.java).subscribe({
            saveRate(it.rate)
        })
    }

    private fun subscribeToRatings() {

        ratesSubscription = ratesDBref
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener,
                com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(
                    snapshot: QuerySnapshot?,
                    exception: FirebaseFirestoreException?
                ) {
                    exception?.let {
                        activity!!.toast("Exception!!! ${exception.message}")
                        return
                    }
                    snapshot?.let {
                        ratesList.clear()
                        val rates = it.toObjects(Rate::class.java)
                        ratesList.addAll(rates)
                        removeFABIfRated(hasUsedRated(ratesList))
                        adapter.notifyDataSetChanged()
                        _view.recyclerView.smoothScrollToPosition(0)
                    }
                }

            })
    }

    private fun hasUsedRated(rates: ArrayList<Rate>): Boolean {
        var result = false
        rates.forEach {
            if (it.userId == currentUser.uid) {
                result = true
            }
        }
        return result
    }

    private fun removeFABIfRated(rated: Boolean) {
        if (rated) {
            _view.fabRating.hide()
            _view.recyclerView.removeOnScrollListener(scrollListener)
        }
    }

    override fun onDestroyView() {
        rateBusListener.dispose()
        ratesSubscription?.remove()
        _view.recyclerView.removeOnScrollListener(scrollListener)
        super.onDestroyView()
    }
}




