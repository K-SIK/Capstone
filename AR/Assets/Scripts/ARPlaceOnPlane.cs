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

    
    void Update()
    {
        //UpdateCenterObject();
        PlaceObjectByTouch();
    }

    private void PlaceObjectByTouch()
    {
        if (Input.touchCount > 0) //��ũ���� ��ġ�� �Ͼ���� Ȯ��(touchCount : ȭ�鿡 ���˵Ǿ� �ִ� �հ��� ����)
        {
            Touch touch = Input.GetTouch(0); //���� ���� ��ġ�� �Ͼ ���� ��ġ�� �Ͼ ��ü ���� ��ȯ
            //��� ��ġ���� ��ġ�� �Ͼ���� ���ð���
            

            //=================================================================================================================================
            //��ġ�� �Ͼ ������ Ray�� ���� ��
            List<ARRaycastHit> hits = new List<ARRaycastHit>(); //hit�� �Ͼ ��ü�� ��ȯ�ޱ� ���� list ����
            if(arRaycaster.Raycast(touch.position, hits, TrackableType.Planes))
            {
                Pose hitPose = hits[0].pose;

                if (!spawnObject) //ù������
                {
                    spawnObject = Instantiate(placeObject, hitPose.position, hitPose.rotation); //��ġ�� �Ͼ �� ������Ʈ�� �߰� �ν��Ͻ�ȭ
                }
                else //ù ������ �������ʹ� ù �����ӿ��� ��ġ�� ��� ������Ʈ �����ִ� ��
                {
                    spawnObject.transform.position = hitPose.position;
                    spawnObject.transform.rotation = hitPose.rotation;
                }
            }
        }
    }

    /*
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
            placeObject.SetActive(false);//����� ������ ������Ʈ�� �Ⱥ��̵���
        }
    }
    */
}
