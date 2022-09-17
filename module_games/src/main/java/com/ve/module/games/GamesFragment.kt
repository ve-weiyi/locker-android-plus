package com.ve.module.games

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.viewmodel.BaseViewModel
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.utils.view.ToastUtil
import com.ve.module.games.aircraftbattle.ui.AircraftBattleActivity
import com.ve.module.games.databinding.FragmentGamesBinding



class GamesFragment : BaseVmListFragment<FragmentGamesBinding, BaseViewModel, Game>() {

    companion object {
        fun newInstance() = GamesFragment()
    }

    val games = mutableListOf(
        Game("飞机大战", R.drawable.plane),
        Game("Apple", R.drawable.apple),
        Game("Banana", R.drawable.banana),
    )
    private val gameList = ArrayList<Game>()

    override fun attachViewModelClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
    }

    override fun attachViewBinding(): FragmentGamesBinding {
        return FragmentGamesBinding.inflate(layoutInflater)
    }


    // private val mAdapter by lazy { GameAdapter() }
    //  private val recyclerView by lazy { mBinding.fragmentRefreshLayout.recyclerView }
    override fun initListView() {
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout

        for (index in 0 until games.size) {
            gameList.add(games[index])
        }

        showAtAdapter(gameList)
        showAtAdapter(false,null)
        //设置行数
//        val layoutManager = GridLayoutManager(activity, 3)
//        recyclerView.layoutManager = layoutManager
//
//        mAdapter.addData(gameList)
//        mAdapter.apply {
//            loadMoreModule.loadMoreEnd(true)
//        }
//        recyclerView.adapter = mAdapter
    }

    override fun loadWebData() {
        super.loadWebData()
        mSwipeRefreshLayout?.isRefreshing=false
        hideLoading()
    }
    override fun initListener() {
        super.initListener()

        mListAdapter.apply {
            setList(gameList)
        }

        mRecyclerView?.apply {
            layoutManager=GridLayoutManager(activity, 3)
        }

    }

    override fun attachAdapter(): BaseQuickAdapter<Game, *> {
        return GamesTagAdapter()
    }

    override fun onItemChildClickEvent(datas: MutableList<Game>, view: View, position: Int) {
        super.onItemChildClickEvent(datas, view, position)
        val game = datas[position]
        goToGame(game)
    }


    override fun onItemClickEvent(datas: MutableList<Game>, view: View, position: Int) {
        super.onItemClickEvent(datas, view, position)
        val game = datas[position]
        goToGame(game)
    }

    private fun goToGame(game: Game) {
        when(game.name){

//            "彩云天气"->{
//                val intent = Intent(context, SunnyActivity::class.java).apply {
//                    putExtra(SunnyActivity.FRUIT_NAME, game.name)
//                    putExtra(SunnyActivity.FRUIT_IMAGE_ID, game.imageId)
//                }
//                startActivity(intent)
//            }
            "飞机大战"->{
                //飞机大战
                startActivity(Intent(context, AircraftBattleActivity::class.java))
            }
            else->
                ToastUtil.show("该功能尚未开放，请留意后续版本.")
        }
    }
}