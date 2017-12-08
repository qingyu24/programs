package logic;

public enum eNoticeType
{
	UNDEFINE,	///<未定义
	GAMBLE,		///<水晶
	ZZZ,
	XFM,
	PK,
	DFWZ,
	BOSS,
	RUNE,
	SPIRIT,
	HELLLEGION,
	;

	public int ID()
	{
		switch(this)
		{
		case UNDEFINE: return 0;
		case GAMBLE: return 1;
		case ZZZ: return 2;
		case XFM: return 3;
		case PK: return 4;
		case DFWZ: return 5;
		case BOSS: return 6;
		case RUNE: return 7;
		case SPIRIT: return 8;
		case HELLLEGION: return 9;
		}
		return -1;
	}
}
