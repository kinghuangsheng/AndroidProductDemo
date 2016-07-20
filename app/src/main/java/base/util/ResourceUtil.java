package base.util;

import android.content.Context;
import android.view.View;

public final class ResourceUtil
{
    
    private ResourceUtil()
    {
        
    }
    
    public static int getResIdByAnimationName(Context context, String name)
    {
        return context.getResources().getIdentifier(name,
            "anim",
            context.getPackageName());
    }
    
    public static int getResIdByStringName(Context context, String name)
    {
        return context.getResources().getIdentifier(name,
            "string",
            context.getPackageName());
    }
    
    public static int getResIdByIdName(Context context, String name)
    {
        return context.getResources().getIdentifier(name,
            "id",
            context.getPackageName());
    }
    
    public static int getResIdByDrawableName(Context context, String name)
    {
        return context.getResources().getIdentifier(name,
            "drawable",
            context.getPackageName());
    }
    
    public static int getResIdByAudioName(Context context, String name)
    {
        return context.getResources().getIdentifier(name,
            "raw",
            context.getPackageName());
    }
    
    public static int getResIdByIdName(View v, String name)
    {
        return v.getResources().getIdentifier(name,
            "id",
            v.getContext().getPackageName());
    }
}
