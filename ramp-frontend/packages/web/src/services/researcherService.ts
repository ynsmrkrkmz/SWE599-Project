import { useMutation, useQuery } from '@tanstack/react-query';
import api from './api';
import {
  Researcher,
  ResearcherList,
  ResearcherListCreateRequest,
  ResearcherListDetail,
  ResearcherListMembershipCreateRequest,
} from 'types/researcherTypes';
import { Apitypes } from 'types';

const baseUrl = 'v1/researchers';

export const useSearchAuthor = (options = {}) =>
  useMutation(
    async (query: string) => {
      return api.fetch<Researcher[]>({
        method: Apitypes.POST,
        url: `${baseUrl}/search-author?query=${query}`,
      });
    },
    {
      ...options,
    }
  );

export const useGetResearcherList = (enabled = true) =>
  useQuery(
    ['researcher-list'],
    async () => {
      return api.fetch<ResearcherList[]>({
        url: `${baseUrl}/researcher-list`,
      });
    },
    {
      enabled,
    }
  );

export const useAddResearcherList = (options = {}) =>
  useMutation(
    async (data: ResearcherListCreateRequest) => {
      return api.fetch<ResearcherList>({
        method: Apitypes.POST,
        url: `${baseUrl}/add-researcher-list`,
        data,
      });
    },
    {
      ...options,
    }
  );

export const useGetResearcherListDetail = (listId: string | undefined, enabled = true) =>
  useQuery(
    ['researcher-list-detail'],
    async () => {
      return api.fetch<ResearcherListDetail>({
        url: `${baseUrl}/researcher-list/${listId}`,
      });
    },
    {
      enabled,
    }
  );

export const useAddResearcherListMembership = (options = {}) =>
  useMutation(
    async (data: ResearcherListMembershipCreateRequest) => {
      return api.fetch({
        method: Apitypes.POST,
        url: `${baseUrl}`,
        data,
      });
    },
    {
      ...options,
    }
  );

export const useRemoveResearcherListMembership = (options = {}) =>
  useMutation(
    async ({ researcherId, listId }: { researcherId: string; listId: string }) => {
      return api.fetch({
        method: Apitypes.DELETE,
        url: `${baseUrl}/remove-from-list?researcherId=${researcherId}&listId=${listId}`,
      });
    },
    {
      ...options,
    }
  );
