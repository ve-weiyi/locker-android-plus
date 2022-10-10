package com.ve.module.games.aircraftbattle.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.ve.lib.common.base.view.vm.BaseActivity;
import com.ve.lib.common.config.AppConfig;
import com.ve.module.games.R;
import com.ve.module.games.aircraftbattle.common.GlobalConstant;
import com.ve.module.games.databinding.ActivityGameBinding;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends BaseActivity<ActivityGameBinding> {

    private GameView gameView;
    private boolean mBackKeyPressed = false;//记录是否有首次按键

    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameView != null) {
            gameView.destroy();
        }
        gameView = null;
    }

    //点击两次退出程序
    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            Toast.makeText(this, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则清除第一次记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {
            Intent intent = new Intent();
            intent.putExtra(GlobalConstant.feijidazhanScore, AppConfig.kv.decodeLong(GlobalConstant.feijidazhanScore));
            setResult(Activity.RESULT_OK, intent);
            //结束Activity
            finish();
        }
    }

    @NonNull
    @Override
    public ActivityGameBinding attachViewBinding() {
        return ActivityGameBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initialize() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)  //状态栏字体是深色，不写默认为亮色
                .init();
        gameView = (GameView) findViewById(R.id.gameView);
        //0:combatAircraft
        //1:explosion
        //2:yellowBullet
        //3:blueBullet
        //4:smallEnemyPlane
        //5:middleEnemyPlane
        //6:bigEnemyPlane
        //7:bombAward
        //8:bulletAward
        //9:pause1
        //10:pause2
        //11:bomb
        int[] bitmapIds = {
                R.drawable.plane,
                R.drawable.explosion,
                R.drawable.yellow_bullet,
                R.drawable.blue_bullet,
                R.drawable.small,
                R.drawable.middle,
                R.drawable.big,
                R.drawable.bomb_award,
                R.drawable.bullet_award,
                R.drawable.pause1,
                R.drawable.pause2,
                R.drawable.bomb
        };
        gameView.start(bitmapIds);
    }
}