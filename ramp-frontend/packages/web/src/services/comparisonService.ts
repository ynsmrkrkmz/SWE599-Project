import { useMutation, useQuery } from '@tanstack/react-query';
import api from './api';
import { Apitypes } from 'types';
import { Comparison, ComparisonCreateRequest, ComparisonDetail } from 'types/comparisonTypes';

const baseUrl = 'v1/comparisons';

export const useGetAllComparison = (enabled = true) =>
  useQuery(
    ['comparisons'],
    async () => {
      return api.fetch<Comparison[]>({
        url: `${baseUrl}`,
      });
    },
    {
      enabled,
    }
  );

export const useAddComparison = (options = {}) =>
  useMutation(
    async (data: ComparisonCreateRequest) => {
      return api.fetch<Comparison>({
        method: Apitypes.POST,
        url: `${baseUrl}`,
        data,
      });
    },
    {
      ...options,
    }
  );

export const useGetComparisonDetail = (comparisonId: string | undefined, enabled = true) =>
  useQuery(
    ['comparison-detail', comparisonId],
    async () => {
      return api.fetch<ComparisonDetail>({
        url: `${baseUrl}/${comparisonId}`,
      });
    },
    {
      enabled,
    }
  );

export const useUpdateComparisonData = (options = {}) =>
  useMutation(
    async ({ comparisonId }: { comparisonId: string }) => {
      return api.fetch({
        method: Apitypes.POST,
        url: `${baseUrl}/update-data?comparisonId=${comparisonId}`,
      });
    },
    {
      ...options,
    }
  );
