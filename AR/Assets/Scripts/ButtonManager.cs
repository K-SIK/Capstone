using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class ButtonManager : MonoBehaviour
{
    private GameObject menu;
    
    public void HideStart()
    {
        Invoke("Hide", 0.1f);
    }

    public void ShowStart()
    {
        Invoke("Show", 0.1f);
    }
    
    private void Hide()
    {
        menu = GameObject.Find("MainMenu");
        menu.SetActive(false);
    }

    private void Show()
    {
        gameObject.SetActive(true);
    }

    

}
