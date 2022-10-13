package com.ve.module.games.aircraftbattle.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.ve.lib.common.base.view.vm.BaseVBActivity;
import com.ve.lib.common.config.AppConfig;
import com.ve.module.games.R;
import com.ve.module.games.aircraftbattle.common.GlobalConstant;
import com.ve.module.games.databinding.ActivityAircraftBattleBinding;


/**
 * 飞机大战
 * @author weiyi
 */
public class AircraftBattleActivity extends BaseVBActivity<ActivityAircraftBattleBinding> implements Button.OnClickListener {

    private int GAMEVIEW = 201;

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnGame) {
            startGame();
        }
    }

    public void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivityForResult(intent, GAMEVIEW);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAMEVIEW && resultCode == Activity.RESULT_OK) {
            mBinding.tvScore.setText("历史最高分：" + data.getLongExtra(GlobalConstant.feijidazhanScore, 0));
        }
    }

    @NonNull
    @Override
    public ActivityAircraftBattleBinding attachViewBinding() {
        return ActivityAircraftBattleBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initialize() {
        ImmersionBar.with(this)
                .init();

        mBinding.tvScore.setText("历史最高分：" + AppConfig.kv.decodeLong(GlobalConstant.feijidazhanScore));
    }

}
