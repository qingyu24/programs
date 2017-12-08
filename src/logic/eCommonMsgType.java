package logic;

public enum eCommonMsgType
{
//	JOINFACTION,	///<加入/离开帮会
//	FACTIONLIST,	///<帮会列表
//	CREATEFACTION,	///<创建帮会
//	FACTIONINFO,	///<我的帮会信息
//	ASKJIONFACTION,	///<申请加入帮会
//	UNASKJIONFACTION,	///<撤销申请加入帮会
//	APPLYLIST,		///<查看审核列表
//	APPLYASKFAIL,	///<批准加入帮会失败
//	MEMBERLIST,		///<查看帮会成员列表
	CDDATA,			///<强化CD数据同步
//	FactionPrayInfo,///<帮会祈福的信息
//	FactionPrayRes,	///<帮会祈福的结果
//	FactionPrayLogChange,///<帮会祈福日志的变更
//	FactionPrayGodLvUp,	 ///<帮会女神升级	
//	FACTIONLOGS,		///<旅团的日志
//	FACTIONDETAILS,		///<旅团的详细信息
//	FACTIONINFORESULT,	///<旅团设置信息的反馈
//	FACTIONTRANSFERDATA,///<旅团查看可转让的对象
//	FACTIONTRANSFER,	///<旅团转让结果
	
	// 采石场水晶相关
	ST_GETQUARRYDATA,	///<获取采石场数据
	ST_GET,				///< 拾取水晶
//	ST_SELL,			///< 出售水晶
//	ST_ONEKEY_GET,		///< 一键拾取
//	ST_MOVE,			///< 移动水晶(包含装备和卸下)
//	ST_SWALLOW,			///< 合成水晶
//	ST_ONEKEY_SWALLOW,	///< 一键合成
	ST_BUYCRYSTAL,		///< 购买获得水晶
	ST_GETCRYSTALDATA,	///< 获取水晶界面数据
	ST_NOTICE,			///< 浮字提示
	ST_APPRAISE_RESULT,	///< 注魔结果
	
	ONLINEAWARD,		///<在线奖励数据
	POSTAWARD,			///<邮寄奖励数据
	POSTAWARDNOTICE,	///<邮寄奖励领取提示
	RESTDATA,			///<休息信息
	RESTEXP,			///<休息经验增加
	RESTQuikenRes,		///<休息加速返回值
	
	STARTSAODANG_SUCCESS,///扫荡开始成功
	ONCESAODANGRECK_OK,	///<扫荡成功
	ONCESAODANGRECK_FAIL,	///<扫荡失败
	ENDSAODANGRECK,		///<结束扫荡
	
	
	JINSIQUESENDTIMES,	///<金丝雀的今日已购次数
	FEEDJINSIQUERES,	///<喂养金丝雀结果

	ACTIVEOPENID, //活动开启
	SYSTEMNOTICE,		///<系统通告
	
	MSGBOXGUIDER_REFRESH,	/// 消息盒子的列表同步
	MSGBOXGUIDER_ENDONE,	/// 消息盒子:结束一个消息盒子

	GETJIHUOAWARD_RESULT,	/// 激活礼包兑换结果
	
	SYSTEMTOPNOTICE,		///<顶上的系统通告
	TREASUREHUNT_RESULT,		///<寻宝的结果
	GetTREASURERMAPCD,		///<领取藏宝图的CD
	GetTREASURERMAP_RESULT,	///<领取藏宝图的结果	
	
	PAYSUBSTITUTE_SUCCESS,	///<使用代币支付成功
	GETZZZPRIZEITEMCID_RESULT,	///<返回转转赚物品ID
	
	RUNE_ANSWER_GRIDINFO,	///<
	RUNE_ANSWER_MULTI_GRIDINFO,
	RUNE_ANSWER_SWAP,
	RUNE_ANSWER_UNLOCK,
	RUNE_ANSWER_INLAY,
	RUNE_ANSWER_BAGINFO,
	RUNE_ANSWER_ALLATTR_BAGINFO,
	RUN_ANSWER_CHANGE,
	RUNE_ANSWER_ADD,
	RUNE_ANSWER_DEL,
	RUNE_ANSWER_Unload,
	RUNE_ANSWER_COMPOUD,
	RUNE_ANSWER_QUREY,
	
	//YDZZ_QUERY,				//云端之战_查询
	YDZZ_ENTER_SUCESS,				//云端之战_进入战斗
	YDZZ_ENTERFAILED,				//云端之战_进入战斗
	//YDZZ_REVIVE,			//云端之战_复活
	
	COLLECTION_QUERY,
	COLLECTION_GOTOCOLLECT,
	COLLECTION_COLLECTFailed,
	COLLECTION_COLLECTSuccess,
	COLLECTION_UNLOCK,
	GETPAYURL_RESULT,
	
	BlessingNum_Change,		//女神祝福数量改变
	
	XFM_RES,
	Friend_OpFail,			// 好友操作失败
	
	Common_Reward,			//通用奖励
	
	Mailer,					//信封提示
	AllUnReadMails,			//所有未读信封
	
	XFM_REFRESH,			//小福猫数据更新
	ZZZ_REFRESH,			//转转赚数据更新
	
	FashionItemList,
	FashionDressingData,
	FashionGet,
	FahsionDress,
	FashionTaskOff,
	
//	FactionDismissInActivity,
//	FactionKickInActivity,
//	
	BagFullNotice,
//
//	Faction_DemonInfo,		//帮会除魔刷新消息
//	Faction_DemonInvite,	//除魔邀请
//	Faction_DemonSummon,	//召唤女神协助除魔
//	Faction_DemonJion,		//加入除魔
//	Faction_EradDevilOK,	//除魔成功
	
	Award_OverFlowInfo,		//优惠礼包信息
	Award_BuyOverFlowGiftRes,		//购买优惠礼包结果
	Award_OnFirstPay,		//首充触发
	Award_GetFirstPayAwardRes,		//获取首充礼包结果
	
	JEWELRY_ANSWER_ITEMLIST, //获取物品列表
	JEWELRY_ANSWER_FIGHTSOUL,//获取战魂数量
	JEWELRY_ANSWER_CHARSDATA,	//获取联合会角色饰品
	JEWELRY_ANSWER_NEWADDITEMS,	//获取新增饰品ID
	JEWELRY_ANSWER_RAISE,	//获取新增饰品ID
	JEWELRY_ANSWER_DECOMPOSE,	//获取新增饰品ID

	Promotion_list,			//或取活动列表
	Promotion_list_Append,		//补充活动列表
	Promotion_Reward_Answer,	//领取活动奖励反馈
	
	Add_Multi_Res,				//奖励增加的反馈(目前仅用于通缉令)
	Hell_Add_Multi_Res,			//地狱军团奖励增加的反馈
	
	MzProgress_Reward_Answer,	//领取历程奖励反馈
	MzProgress_Refresh,			//请求刷新历程数据
	MzProgress_Single_Active,	//完成某一历程

	DELPOSTAWARD,				//通知客户端删除邮寄奖励图标
	
	Item_Limited,				//物品次数受限
	Repay_Award_Res,			//补偿礼包结果
	
	JEWELRY_ANSWER_SMELT,		//战场勋章熔炼
	TIMMINGNOTICE,				// 定时公告
	ACTIVENOTICE,				// 活动公告
	
	MailList_Fail,              //邮件列表
	/////////////////////////////////////////////////////
	;
	
	public int ID()
	{
		switch(this)
		{
//		case JOINFACTION: return 0;
//		case FACTIONLIST: return 1;
//		case CREATEFACTION: return 2;
//		case FACTIONINFO: return 3;
//		case ASKJIONFACTION: return 4;
//		case UNASKJIONFACTION: return 5;
//		case APPLYLIST: return 6;
//		case APPLYASKFAIL: return 7;
//		case MEMBERLIST: return 8;
		case CDDATA: return 9;

		// 采石场水晶相关
		case ST_GETQUARRYDATA: return 10;
		case ST_GET: return 11;				///< 拾取水晶
		case GETPAYURL_RESULT:		return 12;		// 返回充值网址结果

		case TIMMINGNOTICE: return 13;				// 定时公告
		case ACTIVENOTICE: return 14;				// 活动公告

		case ST_BUYCRYSTAL: return 17;		///< 购买获得水晶
		case ST_GETCRYSTALDATA: return 18;	///< 获取水晶界面数据
		case ST_NOTICE: return 19;			///< 浮字提示
		case ST_APPRAISE_RESULT: return 20;	///< 注魔结果

		case ONLINEAWARD: return 21;		///< 在线奖励数据
		case POSTAWARD: return 22;			///< 邮寄奖励数据
		case POSTAWARDNOTICE: return 23;	///< 邮寄奖励数据
		case RESTDATA: return 24;			///< 休息信息
		
		case ONCESAODANGRECK_OK: return 25;	///< 扫荡成功
		case ONCESAODANGRECK_FAIL: return 26;	///< 扫荡失败
		case RESTEXP: return 27;			///< 休息经验增加
		case ENDSAODANGRECK:return 28;		///<扫荡结束
		case JINSIQUESENDTIMES: return 29;	///<金丝雀的今日已购次数
		case FEEDJINSIQUERES: return 30;	///<喂养金丝雀的结果
		
		case SYSTEMNOTICE: return 31;		///<系统通告
		
		case MSGBOXGUIDER_REFRESH: return 32;
		case MSGBOXGUIDER_ENDONE: return 33;
		case GETJIHUOAWARD_RESULT: return 34;
		
		case SYSTEMTOPNOTICE:	return 35;
		case TREASUREHUNT_RESULT: return 36;
		case GetTREASURERMAPCD: return 37;
		case GetTREASURERMAP_RESULT: return 38;
		case PAYSUBSTITUTE_SUCCESS: return 39;
		case GETZZZPRIZEITEMCID_RESULT: return 40;
		case STARTSAODANG_SUCCESS: 	return 41;
		
		case RUNE_ANSWER_GRIDINFO: 	return 42;	///<
		case RUNE_ANSWER_SWAP: 		return 43;
		case RUNE_ANSWER_UNLOCK: 	return 44;
		case RUNE_ANSWER_INLAY: 	return 45;
		case RUNE_ANSWER_BAGINFO: 	return 46;
		case RUNE_ANSWER_ADD:		return 47;
		case RUNE_ANSWER_DEL:		return 48;
		case RUNE_ANSWER_Unload: 	return 49;
		case RUNE_ANSWER_COMPOUD: 	return 50;

		//case YDZZ_QUERY:			return 51;				//云端之战_查询
		case YDZZ_ENTERFAILED:		return 52;				//云端之战_进入战斗 
		//case YDZZ_REVIVE:			return 53;				//云端之战_复活
		
		case RUNE_ANSWER_QUREY: 	return 54;
		
		case COLLECTION_QUERY: 		return 55;
		case COLLECTION_GOTOCOLLECT : return 56;
		case COLLECTION_COLLECTFailed: 	return 57;
		case COLLECTION_UNLOCK:		return 58;	
		
		case YDZZ_ENTER_SUCESS:		return 59;				//云端之战_进入战斗
		
		case RESTQuikenRes:			return 60;		// 加速休息的返回
		
//		case FactionPrayInfo: 		return 61;	//
//		case FactionPrayRes:		return 62;
//		case FactionPrayLogChange:	return 63;
//		case FactionPrayGodLvUp:	return 64;		
		case BlessingNum_Change:	return 65;
		
		case COLLECTION_COLLECTSuccess: return 66;
		
//		case FACTIONLOGS:			return 67;
//		case FACTIONDETAILS:		return 68;
//		case FACTIONINFORESULT:		return 69;
//		case FACTIONTRANSFERDATA:	return 70;
//		case FACTIONTRANSFER:		return 71;
		
		case XFM_RES:				return 72;
		
		case Friend_OpFail:			return 73;
		case Mailer:				return 74;
		case AllUnReadMails:		return 75;		

		case FashionItemList:		return 76;
		case FashionDressingData:	return 77;
		case FashionGet:			return 78;
		case FahsionDress:			return 79;
		case FashionTaskOff:		return 80;
		
//
//		case Faction_DemonInfo: 	return 81;		//帮会除魔刷新消息
//		case Faction_DemonInvite:	return 82;		//除魔邀请
//		case Faction_DemonSummon: 	return 83;		//召唤女神协助除魔
//		case Faction_DemonJion: 	return 84;		//加入除魔
//		case Faction_EradDevilOK: 	return 85;		//除魔成功
		
		case MzProgress_Reward_Answer:	return 90;	//领取历程奖励反馈
		case MzProgress_Refresh:		return 91;	//请求刷新历程数据
		case MzProgress_Single_Active:	return 92;	//某一历程完成
		
		case Promotion_list_Append:	return 99;		//
		case Item_Limited:			return 100;		//物品购买或使用受限
		case Repay_Award_Res:		return 101;		//领取补偿礼包反馈
		
		case Common_Reward:  		return 104;
		
		case XFM_REFRESH:			return 105;
		case ZZZ_REFRESH:			return 106;
		
//		case FactionDismissInActivity:	return 107;
//		case FactionKickInActivity:		return 108;
//		
		case BagFullNotice:				return 109;
		
		case Award_OverFlowInfo:		return 110;
		case Award_BuyOverFlowGiftRes:	return 111;
		case Award_OnFirstPay:		return 112;
		case Award_GetFirstPayAwardRes:		return 113;
		
		
		case JEWELRY_ANSWER_ITEMLIST: return 114;//获取物品列表
		case JEWELRY_ANSWER_FIGHTSOUL: return 115;//获取物品背包中饰品个数
		case JEWELRY_ANSWER_CHARSDATA: return 116;	//获取关联的角色饰品
		case JEWELRY_ANSWER_NEWADDITEMS: return 117;	//获取联合会角色饰品
		case JEWELRY_ANSWER_RAISE: return 118;	//获取联合会角色饰品
		case JEWELRY_ANSWER_DECOMPOSE: return 119;	//获取联合会角色饰品
		/////////////////////////////////////////////////////
		case Promotion_list: return 120;
		case Promotion_Reward_Answer:return 121;
		case Add_Multi_Res:	return 125;
		case Hell_Add_Multi_Res: return 124;
		
		case DELPOSTAWARD: return 126;
		case JEWELRY_ANSWER_SMELT: return 122;	////战场勋章熔炼
		case RUNE_ANSWER_MULTI_GRIDINFO: 	return 123;	///<
		case RUNE_ANSWER_ALLATTR_BAGINFO:   return 124;
		case RUN_ANSWER_CHANGE:				return 125;
		case ACTIVEOPENID:                  return 126;
		}
		return -1;
	}
}
