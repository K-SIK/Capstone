using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ButtonManager : MonoBehaviour
{
    private GameObject menu;


    // Start is called before the first frame update
    public void Hide()
    {
        menu = GameObject.Find("MainMenu");
        menu.SetActive(false);
    }

    public void Show()
    {
        gameObject.SetActive(true);
    }
}
