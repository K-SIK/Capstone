using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.XR.ARFoundation;
using UnityEngine.XR.ARSubsystems;

public class ARPlaceOnPlane : MonoBehaviour
{
    public ARRaycastManager arRaycaster;
    public GameObject placeObject;
    GameObject spawnObject;

    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        //UpdateCenterObject();
        PlaceObjectByTouch();
    }

    private void PlaceObjectByTouch()
    {
        if (Input.touchCount > 0) //touchCount : 화면에 접촉되어 있는 손가락 갯수
        {
            Touch touch = Input.GetTouch(0); //가장 먼저 터치가 일어난 곳의 터치가 일어난 객체 정보 반환
            //어느 위치에서 터치가 일어났는지 얻어올거임
            
            //터치가 일어난 곳으로 Ray를 쏴야 함
            List<ARRaycastHit> hits = new List<ARRaycastHit>();
            if(arRaycaster.Raycast(touch.position, hits, TrackableType.Planes))
            {
                Pose hitPose = hits[0].pose;

                if (!spawnObject) //첫프레임
                {
                    spawnObject = Instantiate(placeObject, hitPose.position, hitPose.rotation); //터치가 일어날 때 오브젝트가 뜨게 인스턴스화
                }
                else //첫 프레임 다음부터는 첫 프레임에서 위치를 계속 업데이트 시켜주는 것
                {
                    spawnObject.transform.position = hitPose.position;
                    spawnObject.transform.rotation = hitPose.rotation;
                }
            }
        }
    }

    private void UpdateCenterObject()
    {
        Vector3 screenCenter = Camera.current.ViewportToScreenPoint(new Vector3(0.5f, 0.5f));

        List<ARRaycastHit> hits = new List<ARRaycastHit>();
        arRaycaster.Raycast(screenCenter, hits, TrackableType.Planes);

        if (hits.Count > 0)
        {
            Pose placementPose = hits[0].pose;
            placeObject.SetActive(true);
            placeObject.transform.SetPositionAndRotation(placementPose.position, placementPose.rotation);
        }
        else
        {
            placeObject.SetActive(false);//평면이 없으면 오브젝트가 안보이도록
        }
    }
}
