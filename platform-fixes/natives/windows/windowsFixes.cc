#include <windows.h>
#include <winuser.h>

// Even if WINVER isn't set high enough for H-WHEEL to be
// defined, the way we use it (to check event-type) is still
// safe -- so just define it ourselves
#ifndef WM_MOUSEHWHEEL
#define WM_MOUSEHWHEEL 0x020E
#endif

#include <unordered_set>
#include "com_metsci_glimpse_platformFixes_WindowsFixes.h"



DWORD _pid = GetCurrentProcessId( );
HANDLE _mutex = CreateMutex( NULL, FALSE, NULL );
std::unordered_set<DWORD> _threadsWithFix;


jstring getErrorString( JNIEnv *env, jint errorCode )
{
    LPVOID buf;
    FormatMessage( FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
                   NULL,
                   errorCode,
                   MAKELANGID( LANG_NEUTRAL, SUBLANG_DEFAULT ),
                   ( LPTSTR ) &buf,
                   0,
                   NULL );
    jstring errorString = env->NewStringUTF( ( LPTSTR ) buf );
    LocalFree( buf );
    return errorString;
}


LRESULT CALLBACK getMsgProc( int nCode, WPARAM wParam, LPARAM lParam )
{
    // Special case -- handle up here so it doesn't get messed with accidentally
    if ( nCode < 0 )
    {
        return CallNextHookEx( NULL, nCode, wParam, lParam );
    }

    MSG *msg = ( MSG * ) lParam;
    if ( nCode == HC_ACTION && ( msg->message == WM_MOUSEWHEEL || msg->message == WM_MOUSEHWHEEL ) )
    {
        HWND hwndDest = NULL;

        // When dragging, wheel events go to the capture window
        DWORD tid = GetWindowThreadProcessId( msg->hwnd, NULL );
        if ( tid == GetCurrentThreadId( ) )
        {
            hwndDest = GetCapture( );
        }
        else
        {
            GUITHREADINFO guiThreadInfo;
            guiThreadInfo.cbSize = sizeof( GUITHREADINFO );
            if ( GetGUIThreadInfo( tid, &guiThreadInfo ) )
            {
                hwndDest = guiThreadInfo.hwndCapture;
            }
        }

        // Otherwise, wheel events go to the hovered window
        if ( !hwndDest )
        {
            hwndDest = WindowFromPoint( msg->pt );
        }

        if ( msg->hwnd != hwndDest )
        {
            // Post an equivalent message to the destination window
            if ( hwndDest )
            {
                DWORD pidHovered;
                GetWindowThreadProcessId( hwndDest, &pidHovered );
                if ( pidHovered == _pid )
                {
                    PostMessage( hwndDest, msg->message, msg->wParam, msg->lParam );
                }
            }

            // Squash the original message
            msg->message = WM_NULL;
        }
    }
    return CallNextHookEx( NULL, nCode, wParam, lParam );
}


LRESULT CALLBACK callWndProc( int nCode, WPARAM wParam, LPARAM lParam )
{
    // Special case -- handle up here so it doesn't get messed with accidentally
    if ( nCode < 0 )
    {
        return CallNextHookEx( NULL, nCode, wParam, lParam );
    }

    CWPSTRUCT *msg = ( CWPSTRUCT * ) lParam;
    if ( nCode == HC_ACTION && msg->message == WM_EXITSIZEMOVE )
    {
        WINDOWPLACEMENT wndPlacement;
        GetWindowPlacement( msg->hwnd, &wndPlacement );

        RECT snappedRect;
        GetWindowRect( msg->hwnd, &snappedRect );

        if ( !EqualRect( &snappedRect, &( wndPlacement.rcNormalPosition ) ) )
        {
            // XXX: Get AWT to fire a window-resize
        }
    }
    return CallNextHookEx( NULL, nCode, wParam, lParam );
}


BOOL CALLBACK setHooksIfNeeded( HWND hwnd, LPARAM lParam )
{
    DWORD pidHwnd;
    DWORD tidHwnd = GetWindowThreadProcessId( hwnd, &pidHwnd );
    if ( pidHwnd == _pid )
    {
        WaitForSingleObject( _mutex, INFINITE );
        {
            if ( _threadsWithFix.find( tidHwnd ) == _threadsWithFix.end( ) )
            {
                HHOOK hGetMsgHook = SetWindowsHookEx( WH_GETMESSAGE, getMsgProc, NULL, tidHwnd );
                if ( hGetMsgHook == NULL )
                {
                    ReleaseMutex( _mutex );
                    return FALSE;
                }

                HHOOK hCallWndHook = SetWindowsHookEx( WH_CALLWNDPROC, callWndProc, NULL, tidHwnd );
                if ( hCallWndHook == NULL )
                {
                    // XXX: This unhook probably trashes the last-error code
                    UnhookWindowsHookEx( hGetMsgHook );
                    ReleaseMutex( _mutex );
                    return FALSE;
                }

                _threadsWithFix.insert( tidHwnd );
            }
        }
        ReleaseMutex( _mutex );
    }
    return TRUE;
}


jstring JNICALL Java_com_metsci_glimpse_platformFixes_WindowsFixes__1applyFixes( JNIEnv *env, jclass jthis )
{
    BOOL success = EnumWindows( setHooksIfNeeded, ( LPARAM ) NULL );
    return ( success ? NULL : getErrorString( env, GetLastError( ) ) );
}
